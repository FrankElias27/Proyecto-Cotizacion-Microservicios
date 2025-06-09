import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators  } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Product } from '../../../model/product';

@Component({
  selector: 'app-edit-product',
  standalone: true,
  imports: [
    CommonModule,
      ReactiveFormsModule,
      MatFormFieldModule,
      MatButtonModule,
      MatDialogModule,
      MatInputModule
  ],
  templateUrl: './edit-product.component.html',
  styleUrl: './edit-product.component.css'
})
export class EditProductComponent {
productForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditProductComponent>,
    @Inject(MAT_DIALOG_DATA) public product: Product
  ) {
    this.productForm = this.fb.group({
      name: [product.name, Validators.required],
      description: [product.description, Validators.required],
      skuCode: [product.skuCode, Validators.required],
      price: [product.price, [Validators.required]],
    });
  }

  save() {
    if (this.productForm.valid) {
      this.dialogRef.close(this.productForm.value);
    }
  }

  cancel() {
    this.dialogRef.close(null);
  }
}


