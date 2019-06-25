import { Component, OnInit, OnDestroy, Input, ElementRef, Output, EventEmitter } from '@angular/core';
import { ModalService } from '../../services/modal.service';
import { ModalRecord } from '../../models/modal-record';

@Component({
  selector: 'jw-modal',
  template:
    `<div class="jw-modal">
      <div class="jw-modal-body">
        <h3>{{title}}</h3>
        <div class="container">
          <button type="button" class="btn btn-sm btn-primary" (click)="load()">Load {{type}}</button>
          <table class="table">
            <thead>
              <tr>
                <th>Select</th>
                <th>Id</th>
                <th>Name</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let record of records">
                <td><input type="radio" name="group" (click)="select(record)"/></td>
                <td>{{record.id}}</td>
                <td>{{record.name}}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <button type="button" class="btn btn-sm btn-primary" (click)="close()">Close</button>
        <button type="button" class="btn btn-sm btn-primary" (click)="choose(); close()">Choose</button>
      </div>
    </div>
    <div class="jw-modal-background"></div>`,
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit, OnDestroy {

  @Input() id: string;
  @Input() type: string;
  @Input() title: string;

  @Output() onSelect = new EventEmitter<ModalRecord>();

  records: ModalRecord[];
  selectedRecord: ModalRecord;


  private element: any;

  constructor(private modalService: ModalService, private el: ElementRef) {
    this.element = el.nativeElement;
    this.records = [{id: 1, name: 'record1'}, {id: 2, name: 'record2'}]
  }

  ngOnInit() {
    console.log(`modal id: ${this.id}`);
    console.log(`modal type: ${this.type}`);
    

    let modal = this;

    // ensure id attribute exists
    if (!this.id) {
      console.error('modal must have an id');
      return;
    }

    // move element to bottom of page (just before </body>) so it can be displayed above everything else
    document.body.appendChild(this.element);

    // add self (this modal instance) to the modal service so it's accessible from controllers
    this.modalService.add(this);

    this.close();

  }

  ngOnDestroy() {
    this.modalService.remove(this.id);
    this.element.remove();
  }

  // open modal
  open(): void {
    this.element.style.display = 'block';
    document.body.classList.add('jw-modal-open');
  }

  // close modal
  close(): void {
    this.element.style.display = 'none';
    document.body.classList.remove('jw-modal-open');
  }

  load() {
    console.log("Loading....");
  }

  select(record: ModalRecord) {
    console.log(record);
    this.selectedRecord = record;
  }

  choose() {
    console.log("selected record:");
    console.log(this.selectedRecord);
    this.onSelect.emit(this.selectedRecord);
  }

}
