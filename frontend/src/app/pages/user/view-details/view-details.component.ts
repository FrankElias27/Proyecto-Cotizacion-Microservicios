import { Component } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Quotation } from '../../../model/quotation';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-view-details',
  standalone: true,
  imports: [MatTableModule, MatPaginator, FormsModule, MatIconModule, MatDialogModule, CommonModule],
  templateUrl: './view-details.component.html',
  styleUrl: './view-details.component.css'
})
export class ViewDetailsComponent {

  displayedColumns: string[] = ['id', 'product', 'quantity', 'subtotal','actions'];
    dataSource = new MatTableDataSource<Quotation>([]);

    totalElements = 0;
    pageSize = 10;
    pageIndex = 0;
    sortActive = 'id';
    sortDirection: 'asc' | 'desc' = 'asc';

    onPageChange(event: PageEvent): void {
        this.pageIndex = event.pageIndex;
        this.pageSize = event.pageSize;


      }


}
