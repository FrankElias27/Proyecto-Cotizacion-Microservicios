import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateQuotationDetailComponent } from './create-quotation-detail.component';

describe('CreateQuotationDetailComponent', () => {
  let component: CreateQuotationDetailComponent;
  let fixture: ComponentFixture<CreateQuotationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateQuotationDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateQuotationDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
