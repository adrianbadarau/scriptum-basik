import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDocumentItem } from 'app/shared/model/document-item.model';
import { DocumentItemService } from './document-item.service';

@Component({
  templateUrl: './document-item-delete-dialog.component.html'
})
export class DocumentItemDeleteDialogComponent {
  documentItem?: IDocumentItem;

  constructor(
    protected documentItemService: DocumentItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentItemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('documentItemListModification');
      this.activeModal.close();
    });
  }
}
