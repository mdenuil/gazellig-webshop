import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorComponent } from './error.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from '../../material.module';

describe('ErrorComponent', () => {
	let component: ErrorComponent;
	let fixture: ComponentFixture<ErrorComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ErrorComponent],
			imports: [
				NoopAnimationsModule,
				MaterialModule,
			]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ErrorComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
