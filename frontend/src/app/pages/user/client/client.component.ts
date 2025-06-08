import { Component, OnInit, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Client } from '../../../model/client';
import { ClientService } from '../../../services/client.service';
import { Page } from '../../../model/page';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditClientComponent } from '../edit-client/edit-client.component';
import { CreateClientComponent } from '../create-client/create-client.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client',
  standalone: true,
  imports: [MatTableModule,MatPaginator, FormsModule,MatIconModule,MatDialogModule,CommonModule],
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'lastname', 'dni','email','phone','address','actions'];
  dataSource = new MatTableDataSource<Client>([]);

  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  sortActive = 'id';
  sortDirection: 'asc' | 'desc' = 'asc';

  searchValue: string = '';

  selectedFile: File | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private clientService: ClientService,private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    const sortParam = `${this.sortActive},${this.sortDirection}`;
    this.clientService.getClients(this.pageIndex, this.pageSize, sortParam).subscribe({
      next: (page: Page<Client>) => {
        this.dataSource.data = page.content;
        this.totalElements = page.totalElements;
      },
      error: (err) => {
        console.error('Error cargando clientes', err);
      }
    });
  }

  searchClients(): void {
    const sortParam = `${this.sortActive},${this.sortDirection}`;
    this.clientService.searchClients(this.searchValue.trim(), this.pageIndex, this.pageSize, sortParam).subscribe({
      next: (page: Page<Client>) => {
        this.dataSource.data = page.content;
        this.totalElements = page.totalElements;
      },
      error: (err) => {
        console.error('Error buscando clientes', err);
      }
    });
  }

   onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;

    if (this.searchValue.trim()) {
      this.searchClients();
    } else {
      this.loadClients();
    }
  }

   applyFilter(): void {
    this.pageIndex = 0;

    if (this.searchValue.trim()) {
      this.searchClients();
    } else {
      this.loadClients();
    }
  }

    openAddClientModal() {
    const dialogRef = this.dialog.open(CreateClientComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.clientService.addClient(result).subscribe({
          next: (newClient) => {
            console.log('Cliente creado:', newClient);
            this.loadClients();
          },
          error: (error) => {
            console.error('Error al crear cliente:', error);
          }
        });
      }
    });
  }

   editClient(id: number) {
    this.clientService.getClientById(id).subscribe(client => {
      const dialogRef = this.dialog.open(EditClientComponent, {
        width: '400px',
        data: client
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          const updatedClient: Client = { ...client, ...result };

          this.clientService.updateClient(id, updatedClient).subscribe({
            next: () => {
              // Actualizar localmente el datasource para reflejar cambios
              const index = this.dataSource.data.findIndex(c => c.id === id);
                if (index !== -1) {
                  this.dataSource.data[index] = updatedClient;
                  this.dataSource.data = [...this.dataSource.data]; // Clonamos para que Angular detecte el cambio
                }

              Swal.fire('¡Éxito!', 'Cliente actualizado correctamente.', 'success');
            },
            error: () => {
              Swal.fire('Error', 'No se pudo actualizar el cliente.', 'error');
            }
          });
        }
      });
    }, () => {
      Swal.fire('Error', 'No se pudo obtener los datos del cliente.', 'error');
    });
  }

   deleteClient(id: number) {
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
        this.clientService.deleteClient(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'Cliente eliminado correctamente',
              timer: 1000,
              showConfirmButton: false
            });
            this.loadClients();
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Error al eliminar cliente'
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

  this.clientService.downloadReport(params).subscribe(blob => {
    const a = document.createElement('a');
    const objectUrl = URL.createObjectURL(blob);
    a.href = objectUrl;

    a.download = 'ReporteClientes.xlsx';

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
    Swal.fire('Formato inválido', 'Solo se permiten archivos .xls o .xlsx', 'warning');
    return;
  }

  this.clientService.importClientsFromExcel(file).subscribe({
    next: (res) => Swal.fire('✅ Éxito', res, 'success'),
    error: (err) => Swal.fire('❌ Error', err.error || 'Algo salió mal', 'error')
  });
}
}
