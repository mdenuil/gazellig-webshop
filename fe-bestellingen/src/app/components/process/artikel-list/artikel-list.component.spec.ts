import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtikelListComponent } from './artikel-list.component';
import { MaterialModule } from 'src/app/material.module';
import { ArtikelService } from 'src/app/services/artikel.service';
import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('ArtikelListComponent', () => {
    let component: ArtikelListComponent;
    let fixture: ComponentFixture<ArtikelListComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [MaterialModule, HttpClientModule],
            providers: [ArtikelService],
            declarations: [ArtikelListComponent],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(ArtikelListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
