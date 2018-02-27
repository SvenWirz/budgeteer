import java.util.List;

public class ReportDTO {
	private List<RowDTO> entries;
	private List<SummaryDTO> summaries;
	public List<RowDTO> getEntries() {
		return entries;
	}
	public void setEntries(List<RowDTO> entries) {
		this.entries = entries;
	}
	public List<SummaryDTO> getSummaries() {
		return summaries;
	}
	public void setSummaries(List<SummaryDTO> summaries) {
		this.summaries = summaries;
	}
	@Override
	public String toString() {
		return "ReportDTO [entries=" + entries + ", summaries=" + summaries + "]";
	}
}
