package at.ac.univie.rscm.application.connection;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public interface PortScannerInterface {
	public int getScanPortBegin();
	public void setScanPortBegin(int port);
	public int getScanPortEnd();
	public void setScanPortEnd(int port);
	public Map<Integer, Date> getOpenPorts();
	public int getTimeSpaceScanningMS();
	public void setTimeSpaceScanningMS(int ms);
	public void start();
	public void scriptsAssignmentHasChaneged();
	public void addEnvironment(int environmentId, int portNumber);
}
