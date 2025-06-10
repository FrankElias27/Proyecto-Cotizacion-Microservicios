import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Quotation } from '../../../model/quotation';
import { QuotationService } from '../../../services/quotation.service';
import { Page } from '../../../model/page';
import { CreateQuotationComponent } from '../create-quotation/create-quotation.component';
import Swal from 'sweetalert2';
import { ViewDetailsComponent } from '../view-details/view-details.component';

@Component({
  selector: 'app-quotation',
  standalone: true,
  imports: [MatTableModule, MatPaginator, FormsModule, MatIconModule, MatDialogModule, CommonModule],
  templateUrl: './quotation.component.html',
  styleUrl: './quotation.component.css'
})
export class QuotationComponent implements OnInit {

  displayedColumns: string[] = ['id', 'client', 'date', 'details', 'total', 'status', 'download', 'actions'];
  dataSource = new MatTableDataSource<Quotation>([]);

  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  sortActive = 'id';
  sortDirection: 'asc' | 'desc' = 'asc';

  searchValue: string = '';

  selectedFile: File | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private quotationService: QuotationService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadQuotations();
  }

  loadQuotations(): void {
    const sortParam = `${this.sortActive},${this.sortDirection}`;
    this.quotationService.getQuotations(this.pageIndex, this.pageSize, sortParam).subscribe({
      next: (page: Page<Quotation>) => {
        this.dataSource.data = page.content;
        this.totalElements = page.totalElements;
      },
      error: (err) => {
        console.error('Error cargando cotizaciones', err);
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;

    if (this.searchValue.trim()) {
    } else {
      this.loadQuotations();
    }
  }

  openAddQuotationModal() {
    const dialogRef = this.dialog.open(CreateQuotationComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.quotationService.addQuotation(result).subscribe({
          next: (newQuotation) => {
            console.log('Cotizacion creada:', newQuotation);
            this.loadQuotations();
            Swal.fire({
              icon: 'success',
              title: '¡Cotizacion guardada!',
              text: 'La Cotizacion ha sido creado exitosamente.',
              confirmButtonText: 'OK'
            });
          },
          error: (error) => {
            console.error('Error al crear cotizacion:', error);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo guardar la cotizacion.'
            });
          }
        });
      }
    });
  }

  openQuotationDetailsModal(quotationId: number, clientId: number): void {
    console.log('Abrir modal con quotationId:', quotationId, 'y clientId:', clientId);
    const dialogRef = this.dialog.open(ViewDetailsComponent, {
      panelClass: 'custom-modal',
      width: '60vw',
      maxWidth: '100%',
      height: '600px',
      data: { quotationId, clientId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'updated') {
        this.loadQuotations();
      }
    });
  }



  deleteQuotation(id: number) {
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
        this.quotationService.deleteQuotation(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'Cotizacion eliminada correctamente',
              timer: 1000,
              showConfirmButton: false
            });
            this.loadQuotations();
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Error al eliminar la cotizacion'
            });
          }
        });
      }
    });
  }

  exportReport(id:number,status:string) {
    console.log(status)
    if (status == 'PROCESSED'){
    const params = {
      cotizacion: id,
      tipo: 'PDF',
    };

    this.quotationService.downloadReport(params).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;

      a.download = 'Cotizacion.pdf';

      a.click();
      URL.revokeObjectURL(objectUrl);
    }, error => {
      console.error('Error al exportar reporte:', error);
    });
  }
  else{

            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'La Cotizacion debe estar en Estado: PROCESSED'
            });

  }
}
}
