import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import Swal from 'sweetalert2';
import { Product } from '../../../model/product';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ProductService } from '../../../services/product.service';
import { Page } from '../../../model/page';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { CreateProductComponent } from '../create-product/create-product.component';
import { EditProductComponent } from '../edit-product/edit-product.component';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [MatTableModule,MatPaginator, FormsModule,MatIconModule,MatDialogModule,CommonModule],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'description', 'skuCode','price','actions'];
    dataSource = new MatTableDataSource<Product>([]);

    totalElements = 0;
    pageSize = 10;
    pageIndex = 0;
    sortActive = 'id';
    sortDirection: 'asc' | 'desc' = 'asc';

    searchValue: string = '';

    selectedFile: File | null = null;

    @ViewChild(MatPaginator) paginator!: MatPaginator;

    constructor(private productService: ProductService,private dialog: MatDialog) {}

    ngOnInit(): void {
      this.loadProducts();
    }

    loadProducts(): void {
      const sortParam = `${this.sortActive},${this.sortDirection}`;
      this.productService.getProducts(this.pageIndex, this.pageSize, sortParam).subscribe({
        next: (page: Page<Product>) => {
          this.dataSource.data = page.content;
          this.totalElements = page.totalElements;
        },
        error: (err) => {
          console.error('Error cargando productos', err);
        }
      });
    }

    searchProducts(): void {
      const sortParam = `${this.sortActive},${this.sortDirection}`;
      this.productService.searchProducts(this.searchValue.trim(), this.pageIndex, this.pageSize, sortParam).subscribe({
        next: (page: Page<Product>) => {
          this.dataSource.data = page.content;
          this.totalElements = page.totalElements;
        },
        error: (err) => {
          console.error('Error buscando productos', err);
        }
      });
    }

     onPageChange(event: PageEvent): void {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;

      if (this.searchValue.trim()) {
        this.searchProducts();
      } else {
        this.loadProducts();
      }
    }

     applyFilter(): void {
      this.pageIndex = 0;

      if (this.searchValue.trim()) {
        this.searchProducts();
      } else {
        this.loadProducts();
      }
    }

      openAddProductModal() {
  const dialogRef = this.dialog.open(CreateProductComponent, {
    width: '400px',
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.productService.addProduct(result).subscribe({
        next: (newProduct) => {
          console.log('Producto creado:', newProduct);
          this.loadProducts();
          Swal.fire({
            icon: 'success',
            title: '¡Producto guardado!',
            text: 'El producto ha sido creado exitosamente.',
            confirmButtonText: 'OK'
          });
        },
        error: (error) => {
          console.error('Error al crear producto:', error);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo guardar el producto.'
          });
        }
      });
    }
  });
}

     editProduct(id: number) {
      this.productService.getProductById(id).subscribe(product => {
        const dialogRef = this.dialog.open(EditProductComponent, {
          width: '400px',
          data: product
        });

        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            const updatedProduct: Product = { ...product, ...result };

            this.productService.updateProduct(id, updatedProduct).subscribe({
              next: () => {
                // Actualizar localmente el datasource para reflejar cambios
                const index = this.dataSource.data.findIndex(c => c.id === id);
                  if (index !== -1) {
                    this.dataSource.data[index] = updatedProduct;
                    this.dataSource.data = [...this.dataSource.data]; // Clonamos para que Angular detecte el cambio
                  }

                Swal.fire('¡Éxito!', 'Producto actualizado correctamente.', 'success');
              },
              error: () => {
                Swal.fire('Error', 'No se pudo actualizar el producto.', 'error');
              }
            });
          }
        });
      }, () => {
        Swal.fire('Error', 'No se pudo obtener los datos del producto.', 'error');
      });
    }

     deleteProduct(id: number) {
      Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esto!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
      }).then((result) => {
        if (result.isConfirmed) {
          this.productService.deleteProduct(id).subscribe({
            next: () => {
              Swal.fire({
                icon: 'success',
                title: 'Eliminado',
                text: 'Producto eliminado correctamente',
                timer: 1000,
                showConfirmButton: false
              });
              this.loadProducts();
            },
            error: (err) => {
              console.error(err);
              Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error al eliminar producto'
              });
            }
          });
        }
      });
    }

    exportReport() {
    const params = {
      tipo: 'EXCEL',
    };

    this.productService.downloadReport(params).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;

      a.download = 'ReporteProductos.xlsx';

      a.click();
      URL.revokeObjectURL(objectUrl);
    }, error => {
      console.error('Error al exportar reporte:', error);
    });
  }



  onFileSelectedAndUpload(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];

    if (!file) return;

    if (!file.name.endsWith('.xls') && !file.name.endsWith('.xlsx')) {
      Swal.fire({
        icon: 'warning',
        title: 'Formato inválido',
        text: 'Solo se permiten archivos .xls o .xlsx',
        confirmButtonColor: '#f39c12',
        confirmButtonText: 'Entendido',
      });
      return;
    }

    this.productService.importProductsFromExcel(file).subscribe({
      next: (res) => {
        Swal.fire({
          icon: 'success',
          title: '¡Importación exitosa!',
          text: res,
          confirmButtonColor: '#28a745',
          confirmButtonText: 'OK',
        }).then((result) => {
          if (result.isConfirmed) {
             this.loadProducts();
          }
        });
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error al importar',
          text: err.error || 'Algo salió mal al procesar el archivo',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Reintentar',
        });
      }
    });
  }

}
