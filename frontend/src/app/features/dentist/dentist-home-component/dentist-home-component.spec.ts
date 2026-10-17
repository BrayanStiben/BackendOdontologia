import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DentistHomeComponent } from './dentist-home-component';

describe('DentistHomeComponent', () => {
  let component: DentistHomeComponent;
  let fixture: ComponentFixture<DentistHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DentistHomeComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DentistHomeComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
