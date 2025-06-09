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

@Component({
  selector: 'app-quotation',
  standalone: true,
  imports: [MatTableModule,MatPaginator, FormsModule,MatIconModule,MatDialogModule,CommonModule],
  templateUrl: './quotation.component.html',
  styleUrl: './quotation.component.css'
})
export class QuotationComponent implements OnInit {

  displayedColumns: string[] = ['id', 'client', 'subject', 'date','details','total','actions'];
    dataSource = new MatTableDataSource<Quotation>([]);

    totalElements = 0;
    pageSize = 10;
    pageIndex = 0;
    sortActive = 'id';
    sortDirection: 'asc' | 'desc' = 'asc';

    searchValue: string = '';

    selectedFile: File | null = null;

    @ViewChild(MatPaginator) paginator!: MatPaginator;

    constructor(private quotationService: QuotationService,private dialog: MatDialog) {}

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
}
