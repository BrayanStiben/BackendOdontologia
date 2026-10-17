import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConsultationComponent } from './consultation-component';

describe('ConsultationComponent', () => {
  let component: ConsultationComponent;
  let fixture: ComponentFixture<ConsultationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultationComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ConsultationComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
