import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Client } from '../../../model/client';
import { ClientService } from '../../../services/client.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@Component({
  selector: 'app-create-quotation',
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
  templateUrl: './create-quotation.component.html',
  styleUrl: './create-quotation.component.css'
})
export class CreateQuotationComponent implements OnInit {

  quotationForm!: FormGroup;
  filteredClients: Client[] = [];
  allClients: Client[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CreateQuotationComponent>,
    private clientService: ClientService
  ) { }

  ngOnInit() {
    this.quotationForm = this.fb.group({
      client: [null, Validators.required],
      subject: ['', Validators.required],
    });
    this.loadClients();

  }

  loadClients(): void {
    this.clientService.getAllClients().subscribe(clients => {
      this.allClients = clients;

      this.quotationForm.get('client')?.valueChanges.subscribe(value => {
        const filterValue = typeof value === 'string'
          ? value.toLowerCase()
          : value?.name?.toLowerCase() ?? '';

        this.filteredClients = this.allClients.filter(client =>
          client.firstName.toLowerCase().includes(filterValue) ||
          client.lastName.toLowerCase().includes(filterValue)
        );
      });
    });
  }

  onClientSelected(selectedClient: Client) {
    this.quotationForm.get('client')?.setValue(selectedClient);
  }

  displayClientFn(client: Client | string): string {
    return typeof client === 'string'
      ? client
      : client ? `${client.firstName} ${client.lastName}` : '';
  }

  onSubmit() {
  if (this.quotationForm.valid) {
    const formValue = this.quotationForm.value;

    const payload = {
      clientId: formValue.client.id,
      subject: formValue.subject,
    };

    this.dialogRef.close(payload);
  }
}

  onCancel() {
    this.dialogRef.close();
  }
}
