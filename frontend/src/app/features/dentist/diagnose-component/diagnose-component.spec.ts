import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DiagnoseComponent } from './diagnose-component';

describe('DiagnoseComponent', () => {
  let component: DiagnoseComponent;
  let fixture: ComponentFixture<DiagnoseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiagnoseComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiagnoseComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
