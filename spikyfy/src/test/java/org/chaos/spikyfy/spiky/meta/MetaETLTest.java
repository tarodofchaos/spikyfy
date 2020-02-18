package org.chaos.spikyfy.spiky.meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.chaos.spikyfy.spiky.model.BandPerGenre;
import org.chaos.spikyfy.spiky.util.AppConstants;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.github.javafaker.Faker;

public class MetaETLTest {
	
	String countries[];
	double probabilities[];
	RandomCountry rc;
	AmazonDynamoDB client;
	DynamoDBMapper mapper;
	Faker faker;
	Map<String, String> userCountry;
	
	@Before
	public void setUp() {
		client = AmazonDynamoDBClientBuilder.standard().withRegion("eu-west-1").build();
		mapper = new DynamoDBMapper(client);
		faker = new Faker();
		userCountry = new HashMap<>();
		
		countries = new String[] { "China","India","United States of America","Pakistan","Brazil","Nigeria","Russia","Mexico","Japan","Philippines","Turkey","Germany","United Kingdom","France","Italy","South Africa","Kenya","South Korea","Colombia","Spain","Argentina","Algeria","Ukraine","Afghanistan","Poland","Canada","Morocco","Peru","Australia","Romania","Chile","Ecuador","Syria","Netherlands","Senegal","Zimbabwe","Guinea","Tunisia","Bolivia","Belgium","Haiti","Cuba","Dominican Republic","Czechia (Czech Republic)","Greece","Portugal","Azerbaijan","Sweden","Honduras","Hungary","Tajikistan","Belarus","Austria","Serbia","Switzerland","Laos","Paraguay","Bulgaria","Lebanon","Nicaragua","El Salvador","Turkmenistan","Singapore","Denmark","Finland","Slovakia","Norway","Costa Rica","Ireland","New Zealand","Panama","Croatia","Georgia","Eritrea","Uruguay","Bosnia and Herzegovina","Mongolia","Armenia","Jamaica","Albania","Lithuania","Botswana","Lesotho","Slovenia","Guinea-Bissau","Latvia","Bahrain","Equatorial Guinea","Trinidad and Tobago","Estonia","Mauritius","Cyprus","Fiji","Solomon Islands","Montenegro","Luxembourg","Cabo Verde","Maldives","Bahamas","Iceland" };
		probabilities = new double[] {0.01,0.002,0.16,0.002,0.01,0.002,0.01,0.01,0.02,0.002,0.002,0.09,0.12,0.02,0.03,0.002,0.002,0.002,0.002,0.04,0.02,0.002,0.002,0.002,0.01,0.07,0.002,0.002,0.02,0.002,0.002,0.002,0.002,0.02,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.01,0.01,0.002,0.02,0.002,0.002,0.002,0.002,0.01,0.002,0.01,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.04,0.002,0.06,0.002,0.02,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.002,0.01};
		rc = new RandomCountry(countries, probabilities);
	}

	@Test
	public void generateJsonStringForDynamoDB() {
		BandPerGenre band = new BandPerGenre();
		List<BandPerGenre> bandList = new ArrayList<>();
		int i = 1;
		
		try {
			List<Path> files = Files.list(FileSystems.getDefault().getPath("D:\\16. Dev\\Zartis\\metal_dataset-master\\bands\\short")).collect(Collectors.toList());
			files.get(0).getFileName();
			for (Path file : files) {
				File f = file.toFile();
				FileReader fr=new FileReader(f);
				BufferedReader br=new BufferedReader(fr);
				String line;  
				while((line=br.readLine())!=null)  
				{  
					band = new BandPerGenre();
					band.setId(i++);
					band.setBandGenre(file.getFileName().toString());
					band.setBandName(line);
					band.setBandCountry(rc.random());
					bandList.add(band);
				}  
				fr.close();  
				System.out.println("");
				mapper.batchSave(bandList);
				bandList.clear();
			} 
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void generateSampleFileFromDynamoDB() {
		List<BandPerGenre> bandList;
		try {
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
			
			bandList = mapper.scan(BandPerGenre.class, scanExpression);
			String band;
			String country;
			String username;
			StringBuilder sb = new StringBuilder();
			
			FileWriter writer = new FileWriter("D:\\16. Dev\\Zartis\\spikyfy\\output\\load.in", true);
			
			for (int i = 0; i < 2000000; i++) {
				if (i % 10000 == 0) {
					System.out.println(new Date().toString()+" "+i+" records created");
				}
				band = bandList.get(getRandomNumberInRange(0, 2299)).getBandName();
				username = getRandomUsername(getRandomNumberInRange(1, 10));
				country = userCountry.get(username);
				sb.append(username).append(AppConstants.SEPARATOR).append(band).append(AppConstants.SEPARATOR).append(country).append("\r\n");
			}
			writer.write(sb.toString());
			writer.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	private String getRandomUsername(int type) {
		String username = "";
		switch (type) {
		case 1:
			username = faker.ancient().god();
			break;
		case 2:
			username = faker.artist().name();
			break;
		case 3:
			username = faker.backToTheFuture().character();
			break;
		case 4:
			username = faker.dragonBall().character();
			break;
		case 5:
			username = faker.dune().character();
			break;
		case 6:
			username = faker.elderScrolls().dragon();
			break;
		case 7:
			username = faker.hipster().word();
			break;
		case 8:
			username = faker.pokemon().name();
			break;
		case 9:
			username = faker.lordOfTheRings().character();
			break;
		case 10:
			username = faker.witcher().monster();
			break;
			
		default:
			break;
		}
		
		username = username.replace(" ", "").toLowerCase();
		
		if (!userCountry.containsKey(username)) {
			userCountry.put(username, countries[getRandomNumberInRange(0, 99)]);
		}		
		return username;
	}
}
