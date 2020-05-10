import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentItem } from 'app/shared/model/document-item.model';

@Component({
  selector: 'jhi-document-item-detail',
  templateUrl: './document-item-detail.component.html'
})
export class DocumentItemDetailComponent implements OnInit {
  documentItem: IDocumentItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentItem }) => (this.documentItem = documentItem));
  }

  previousState(): void {
    window.history.back();
  }
}
