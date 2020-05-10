import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ScriptumBasikSharedModule } from 'app/shared/shared.module';
import { DocumentItemComponent } from './document-item.component';
import { DocumentItemDetailComponent } from './document-item-detail.component';
import { DocumentItemUpdateComponent } from './document-item-update.component';
import { DocumentItemDeleteDialogComponent } from './document-item-delete-dialog.component';
import { documentItemRoute } from './document-item.route';

@NgModule({
  imports: [ScriptumBasikSharedModule, RouterModule.forChild(documentItemRoute)],
  declarations: [DocumentItemComponent, DocumentItemDetailComponent, DocumentItemUpdateComponent, DocumentItemDeleteDialogComponent],
  entryComponents: [DocumentItemDeleteDialogComponent]
})
export class ScriptumBasikDocumentItemModule {}
