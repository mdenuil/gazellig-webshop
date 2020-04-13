import { async, TestBed } from '@angular/core/testing';

import { AccountService } from './account.service';

import { HttpClientModule } from '@angular/common/http';

describe('AccountService', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule]
    })
    .compileComponents();
  }));

  it('should be created', () => {
    const service: AccountService = TestBed.get(AccountService);
    expect(service).toBeTruthy();
  });
});
