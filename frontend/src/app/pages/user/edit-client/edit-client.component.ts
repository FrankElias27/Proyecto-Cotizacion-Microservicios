import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators  } from '@angular/forms';
import { Client } from '../../../model/client';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-edit-client',
  standalone: true,
  imports: [
  CommonModule,
  ReactiveFormsModule,
  MatFormFieldModule,
  MatButtonModule,
  MatDialogModule,
  MatInputModule
  ],
  templateUrl: './edit-client.component.html',
  styleUrl: './edit-client.component.css'
})
export class EditClientComponent {
  clientForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditClientComponent>,
    @Inject(MAT_DIALOG_DATA) public client: Client
  ) {
    this.clientForm = this.fb.group({
      firstName: [client.firstName, Validators.required],
      lastName: [client.lastName, Validators.required],
      dni: [client.dni, Validators.required],
      email: [client.email, [Validators.required, Validators.email]],
      phone: [client.phone],
      address: [client.address]
    });
  }

  save() {
    if (this.clientForm.valid) {
      this.dialogRef.close(this.clientForm.value);
    }
  }

  cancel() {
    this.dialogRef.close(null);
  }
}

