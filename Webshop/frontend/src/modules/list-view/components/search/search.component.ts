import {Component, EventEmitter, Output} from "@angular/core";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'search',
  templateUrl: 'search.component.html',
  styleUrls: ['search.component.scss']
})
export class SearchComponent {
  @Output() submitSearch = new EventEmitter<string>();

  nameSearch = new FormControl('', [Validators.required]);
  searchForm = new FormGroup({
    nameSearch: this.nameSearch
  })

  onSearchSubmit(): void {
    this.submitSearch.emit(this.searchForm.value.nameSearch!);
  }
}
