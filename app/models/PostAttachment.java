package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import play.db.jpa.Model;

@Entity
@Table(name = "post_attachment")
public class PostAttachment extends Model{
	
	@Column(nullable = false)
	@NotNull
	public String url;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	@NotNull
	public AttachmentType type = AttachmentType.IMAGE;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id", referencedColumnName="id", nullable = false)
	public Post post;
}
