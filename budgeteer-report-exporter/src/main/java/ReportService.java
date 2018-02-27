import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ReportService {
	
	private static int counter = 0;
	
	public ReportDTO getDummyData() {
		ReportDTO report = new ReportDTO();
		report.setEntries(Arrays.asList(getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry(),getDummyEntry()));
		report.setSummaries(getSummariesOf(report.getEntries()));
		return report;
	}
	
	private List<SummaryDTO> getSummariesOf(List<RowDTO> entries) {
		HashMap<String,Double> map = new HashMap<String,Double>();
		for(RowDTO row : entries) {
			if(map.containsKey(row.getRechnungsEmpfaenger())) {
				double sum = map.get(row.getRechnungsEmpfaenger()) + row.getBrutto();
				map.put(row.getRechnungsEmpfaenger(), sum);
			}
			else {
				map.put(row.getRechnungsEmpfaenger(), row.getBrutto());
			}
		}
		
		return map.entrySet().stream().map(entry -> new SummaryDTO(entry.getKey())).collect(Collectors.toList());
	}

	public RowDTO getDummyEntry() {
		Random rng =new Random();
		RowDTO row = new RowDTO();
		row.setBrutto(rng.nextDouble());
		row.setNetto(rng.nextDouble());
		row.setHours(rng.nextInt(10));
		row.setNettoRestmenge(rng.nextDouble());
		row.setBruttoRestmenge(rng.nextDouble());
		row.setLeistungsstand(rng.nextDouble());
		row.setBudget("BudgetTest");
		row.setFrom(getDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1).plus(counter++, ChronoUnit.MONTHS)));
		row.setUntil(getDate(LocalDate.of(LocalDate.now().getYear()+1, LocalDate.now().getMonth(), 1).plus(counter, ChronoUnit.MONTHS)));
		if(counter % 2 == 0)
			row.setRechnungsEmpfaenger("Test");
		else 
			row.setRechnungsEmpfaenger("Test2");
		return row;
	}
	
	public static Date getDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
