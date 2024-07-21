import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositWithdrawDialogComponent } from './deposit-withdraw-dialog.component';

describe('DepositWithdrawDialogComponent', () => {
  let component: DepositWithdrawDialogComponent;
  let fixture: ComponentFixture<DepositWithdrawDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DepositWithdrawDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DepositWithdrawDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
