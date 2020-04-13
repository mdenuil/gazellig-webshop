import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadingComponent } from './loading.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from '../../material.module';

describe('LoadingComponent', () => {
	let component: LoadingComponent;
	let fixture: ComponentFixture<LoadingComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [LoadingComponent],
			imports: [
				NoopAnimationsModule,
				MaterialModule,
			]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(LoadingComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
