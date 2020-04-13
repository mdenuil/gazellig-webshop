import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ArtikelService } from './services/artikel.service';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from './material.module';
import { ProcessComponent } from './components/process/process.component';
import { ArtikelListComponent } from './components/process/artikel-list/artikel-list.component';

describe('AppComponent', () => {
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
                MaterialModule,
                NoopAnimationsModule,
                HttpClientModule
            ],
            declarations: [
                AppComponent,
                ProcessComponent,
                ArtikelListComponent
            ],
            providers: [
                ArtikelService
            ]
        }).compileComponents();
    }));

    it('should create the app', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.debugElement.componentInstance;
        expect(app).toBeTruthy();
    });
});
