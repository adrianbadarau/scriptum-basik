import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document-item',
        loadChildren: () => import('./document-item/document-item.module').then(m => m.ScriptumBasikDocumentItemModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ScriptumBasikEntityModule {}
