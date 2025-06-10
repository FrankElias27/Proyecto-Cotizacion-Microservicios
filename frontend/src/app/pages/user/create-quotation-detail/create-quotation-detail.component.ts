import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule,Validators } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Product } from '../../../model/product';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-create-quotation-detail',
  standalone: true,
  imports: [
    CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatButtonModule,
        MatDialogModule,
        MatInputModule,
        MatAutocompleteModule
  ],
  templateUrl: './create-quotation-detail.component.html',
  styleUrl: './create-quotation-detail.component.css'
})
export class CreateQuotationDetailComponent implements OnInit {

  quotationDetailForm!: FormGroup;
  filteredProducts: Product[] = [];
  allProducts: Product[] = [];

  constructor(
     @Inject(MAT_DIALOG_DATA) public data: { quotationId: number },
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CreateQuotationDetailComponent>,
    private productService:ProductService
  ) { }

  ngOnInit() {
    this.quotationDetailForm = this.fb.group({
      product: [null, Validators.required],
      quantity: ['', Validators.required],
    });
    this.loadProducts();

  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe(products => {
      this.allProducts = products;

      this.quotationDetailForm.get('product')?.valueChanges.subscribe(value => {
        const filterValue = typeof value === 'string'
          ? value.toLowerCase()
          : value?.name?.toLowerCase() ?? '';

        this.filteredProducts = this.allProducts.filter(product =>
          product.name.toLowerCase().includes(filterValue)
        );
      });
    });
  }

  onProductSelected(selectedProduct: Product) {
    this.quotationDetailForm.get('product')?.setValue(selectedProduct);
  }

  displayProductFn(product: Product | string): string {
    return typeof product === 'string'
      ? product
      : product ? `${product.name}` : '';
  }

  onSubmit() {
  if (this.quotationDetailForm.valid) {
    const formValue = this.quotationDetailForm.value;
    console.log("quotationId:",this.data.quotationId)

    const payload = {
      quotationId: this.data.quotationId,
      productId: formValue.product.id,
      quantity: formValue.quantity,
    };

    this.dialogRef.close(payload);
  }
}

  onCancel() {
    this.dialogRef.close();
  }


}
