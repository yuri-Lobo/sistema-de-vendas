import { TestBed } from '@angular/core/testing';

import { VendaProdutoService } from './venda-produto.service';

describe('VendaProdutoService', () => {
  let service: VendaProdutoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VendaProdutoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
