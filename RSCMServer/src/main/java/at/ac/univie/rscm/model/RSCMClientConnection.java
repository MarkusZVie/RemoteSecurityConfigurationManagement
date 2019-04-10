package at.ac.univie.rscm.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "client_connectionlog")
public class RSCMClientConnection {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int logId;
		private Date connectionStart;
		private Date connectionEnd;
		private String connectionExitCode;
		private String connectionDescription;
		@ManyToOne
	    @JoinColumn(name="rscmclient_fs", nullable=false)
		private RSCMClient rscmClient;

}
