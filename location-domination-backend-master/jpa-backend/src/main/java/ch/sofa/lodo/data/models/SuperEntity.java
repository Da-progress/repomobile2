package ch.sofa.lodo.data.models;

import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings("unused")
@MappedSuperclass
public class SuperEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	@Column(name = "record_version")
	private int recordVersion = 1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(int recordVersion) {
		this.recordVersion = recordVersion;
	}

	/**
	 * If to entity instances of one class have same {@link #id primary key} they
	 * are considered equal.
	 * <p>
	 * Two transient entities are never equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SuperEntity other = (SuperEntity) obj;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Do override when special copy is needed.
	 *
	 * @param source source
	 */
	public void copyPropertiesFrom(SuperEntity source) {
		BeanUtils.copyProperties(source, this);
	}
}
