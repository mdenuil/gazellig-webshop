import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinishedComponent } from './finished.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from '../../material.module';
import { ReactiveFormsModule } from '@angular/forms';

describe('FinishedComponent', () => {
	let component: FinishedComponent;
	let fixture: ComponentFixture<FinishedComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [FinishedComponent],
			imports: [
				NoopAnimationsModule,
				MaterialModule,
				ReactiveFormsModule
			]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(FinishedComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
