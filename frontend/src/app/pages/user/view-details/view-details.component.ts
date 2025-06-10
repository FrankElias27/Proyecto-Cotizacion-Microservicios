import { Component, Inject, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Quotation } from '../../../model/quotation';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { QuotationDetail } from '../../../model/quotationDetail';
import { QuotationDetailService } from '../../../services/quotation-detail.service';
import { Page } from '../../../model/page';
import { CreateQuotationDetailComponent } from '../create-quotation-detail/create-quotation-detail.component';
import Swal from 'sweetalert2';
import { QuotationService } from '../../../services/quotation.service';

@Component({
  selector: 'app-view-details',
  standalone: true,
  imports: [MatTableModule, MatPaginator, FormsModule, MatIconModule, MatDialogModule, CommonModule],
  templateUrl: './view-details.component.html',
  styleUrl: './view-details.component.css'
})
export class ViewDetailsComponent {

  displayedColumns: string[] = ['id', 'product', 'quantity', 'unitPrice', 'subtotal', 'actions'];
  dataSource = new MatTableDataSource<QuotationDetail>([]);

  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  sortActive = 'id';
  sortDirection: 'asc' | 'desc' = 'asc';
  currentPage = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { quotationId: number,clientId:number },
    private quotationDetailService: QuotationDetailService, private dialog: MatDialog,
    private quotationService:QuotationService, private dialogRef: MatDialogRef<ViewDetailsComponent>
  ) { }

  ngOnInit(): void {
    this.loadDetails();
  }

  loadDetails(): void {
    const sortParam = `${this.sortActive},${this.sortDirection}`;
    this.quotationDetailService.getByQuotationId(this.data.quotationId, this.pageIndex, this.pageSize, sortParam).subscribe({
      next: (result: Page<QuotationDetail>) => {
        this.dataSource.data = result.content;
        this.totalElements = result.totalElements;
      },
      error: err => {
        console.error('Error al cargar detalles de cotización:', err);
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadDetails();
  }

  openAddQuotationDetailModal() {
    console.log('Enviando quotationId al modal:', this.data.quotationId);
    const dialogRef = this.dialog.open(CreateQuotationDetailComponent, {
      width: '400px',
      data: { quotationId: this.data.quotationId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.quotationDetailService.addQuotationDetail(result).subscribe({
          next: (newQuotation) => {
            console.log('Detalle Cotizacion creado:', newQuotation);
            this.loadDetails();
            Swal.fire({
              icon: 'success',
              title: '¡Detalle guardado!',
              text: 'El detalle ha sido creado exitosamente.',
              confirmButtonText: 'OK'
            });
          },
          error: (error) => {
            console.error('Error al crear detalle:', error);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo guardar el detalle.'
            });
          }
        });
      }
    });
  }

  updateQuotationTotal(): void {
  const quotationId = this.data.quotationId;
  const clientId=this.data.clientId

  this.quotationDetailService.getDetailsByQuotationId(quotationId).subscribe({
    next: (details) => {
      const total = details.reduce((sum, detail) => sum + detail.subtotal, 0);

      this.quotationService.getQuotationById(quotationId).subscribe({
        next: (quotation) => {
          const updatedQuotation = { ...quotation, total,clientId };

          this.quotationService.updateQuotation(quotationId, updatedQuotation).subscribe({
             next: () => {
              Swal.fire({
                icon: 'success',
                title: 'Cotización procesada',
                text: `El total de la cotización se actualizó a S/ ${total.toFixed(2)}.`
              }).then(() => {
                this.dialogRef.close('updated');
              });
            },
            error: (err) => {
              Swal.fire({
                icon: 'error',
                title: 'Error al procesar',
                text: 'Ocurrió un error al actualizar la cotización.'
              });
              console.error('Error al actualizar cotización:', err);
            }
          });
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al obtener cotización',
            text: 'No se pudo obtener la cotización.'
          });
          console.error('Error al obtener cotización:', err);
        }
      });
    },
    error: (err) => {
      Swal.fire({
        icon: 'error',
        title: 'Error al obtener detalles',
        text: 'No se pudieron obtener los detalles de la cotización.'
      });
      console.error('Error al obtener detalles de cotización:', err);
    }
  });
}


  deleteQuotationDetail(id: number) {
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
        this.quotationDetailService.deleteQuotationDetail(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'Detalle eliminado correctamente',
              timer: 1000,
              showConfirmButton: false
            });
            this.loadDetails();
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Error al eliminar el detalle'
            });
          }
        });
      }
    });
  }
}

