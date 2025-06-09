import { TestBed } from '@angular/core/testing';

import { QuotationDetailService } from './quotation-detail.service';

describe('QuotationDetailService', () => {
  let service: QuotationDetailService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuotationDetailService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
