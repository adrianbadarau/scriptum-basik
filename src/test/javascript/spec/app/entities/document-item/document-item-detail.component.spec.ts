import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ScriptumBasikTestModule } from '../../../test.module';
import { DocumentItemDetailComponent } from 'app/entities/document-item/document-item-detail.component';
import { DocumentItem } from 'app/shared/model/document-item.model';

describe('Component Tests', () => {
  describe('DocumentItem Management Detail Component', () => {
    let comp: DocumentItemDetailComponent;
    let fixture: ComponentFixture<DocumentItemDetailComponent>;
    const route = ({ data: of({ documentItem: new DocumentItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ScriptumBasikTestModule],
        declarations: [DocumentItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DocumentItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
