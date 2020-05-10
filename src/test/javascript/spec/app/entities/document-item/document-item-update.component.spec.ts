import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ScriptumBasikTestModule } from '../../../test.module';
import { DocumentItemUpdateComponent } from 'app/entities/document-item/document-item-update.component';
import { DocumentItemService } from 'app/entities/document-item/document-item.service';
import { DocumentItem } from 'app/shared/model/document-item.model';

describe('Component Tests', () => {
  describe('DocumentItem Management Update Component', () => {
    let comp: DocumentItemUpdateComponent;
    let fixture: ComponentFixture<DocumentItemUpdateComponent>;
    let service: DocumentItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ScriptumBasikTestModule],
        declarations: [DocumentItemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DocumentItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocumentItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DocumentItem(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new DocumentItem();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
