import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraderFormDialogComponent } from './trader-form-dialog.component';

describe('TraderFormDialogComponent', () => {
  let component: TraderFormDialogComponent;
  let fixture: ComponentFixture<TraderFormDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TraderFormDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TraderFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
