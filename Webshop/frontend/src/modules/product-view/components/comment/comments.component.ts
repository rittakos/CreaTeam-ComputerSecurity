import {Component, EventEmitter, Input, Output} from "@angular/core";
import {CommentResponse} from "../../../../app/api/webshop/models/comment-response";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'comments',
  templateUrl: 'comments.component.html',
  styleUrls: ['comments.component.scss']
})
export class CommentsComponent{
  @Input() comments!: CommentResponse[];
  @Output() submitComment = new EventEmitter<string>();


  commentText = new FormControl('', [Validators.required]);
  commentForm: FormGroup = new FormGroup({
    commentText: this.commentText
  })

  onCommentSubmit(): void {
    this.submitComment.emit(this.commentForm.value.commentText);
  }
}
