package org.vaadin.sliderpanel.demo.data;

import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class DummyDataGen {

	public static final List<Inhabitants> genInhabitants(final int quantity) {
		List<Inhabitants> result = new ArrayList<Inhabitants>();

		for (long x = 1; x <= quantity; x++) {
			result.add(genInhabitant(x));
		}

		return result;
	}

	public static final Inhabitants genInhabitant(final long id) {
		Inhabitants inh = new Inhabitants(id, Math.random() > 0.5 ? Inhabitants.Gender.FEMALE : Inhabitants.Gender.MALE);
		if (inh.getGender()
			   .equals(Inhabitants.Gender.MALE)) {
			inh.setName(randomOfList(MALES));
		} else {
			inh.setName(randomOfList(FEMALES));
		}
		inh.setBirthday(new Date(new Date().getTime() - ((long) (Math.random() * 10000) % (365 * 90)) * DateUtils.MILLIS_PER_DAY));
		inh.setBodySize(1.6 + (Math.random() * (Math.random() > 0.5 ? -1 : +1)));
		inh.setOnFacebook(Math.random() > 0.5);
		return inh;
	}

	private final static String randomOfList(final List<String> list) {
		return list.get((int) (Math.random() * 10000) % (list.size() - 1));
	}

	public static List<String> FEMALES = Arrays.asList("AMELIA", "OLIVIA", "JESSICA", "EMILY", "LILY", "AVA", "MIA", "ISLA", "SOPHIE", "ISABELLA", "EVIE",
			"RUBY", "POPPY", "GRACE", "SOPHIA", "CHLOE", "ISABELLE", "ELLA", "FREYA", "CHARLOTTE", "SCARLETT", "DAISY", "LOLA", "EVA", "HOLLY", "MILLIE",
			"LUCY", "PHOEBE", "LAYLA", "MAISIE", "SIENNA", "ALICE", "LILLY", "FLORENCE", "ELLIE", "ERIN", "IMOGEN", "ELIZABETH", "MOLLY", "SUMMER", "MEGAN",
			"HANNAH", "SOFIA", "ABIGAIL", "JASMINE", "LEXI", "MATILDA", "ROSIE", "LACEY", "EMMA", "AMELIE", "GRACIE", "MAYA", "HOLLIE", "GEORGIA", "EMILIA",
			"EVELYN", "BELLA", "BROOKE", "AMBER", "ELIZA", "AMY", "ELEANOR", "LEAH", "ESME", "KATIE", "HARRIET", "ANNA", "WILLOW", "ELSIE", "ZARA", "ANNABELLE",
			"BETHANY", "FAITH", "MADISON", "ISABEL", "MARTHA", "ROSE", "JULIA", "PAIGE", "MARYAM", "MADDISON", "HEIDI", "MOLLIE", "NIAMH", "SKYE", "AISHA",
			"IVY", "DARCEY", "FRANCESCA", "ZOE", "KEIRA", "TILLY", "MARIA", "SARAH", "LYDIA", "CAITLIN", "ISOBEL", "SARA", "VIOLET");

	public static List<String> MALES = Arrays.asList("OLIVER", "JACK", "CHARLIE", "JACOB", "THOMAS", "ALFIE", "RILEY", "WILLIAM", "JAMES", "JOSHUA", "GEORGE",
			"ETHAN", "NOAH", "SAMUEL", "DANIEL", "OSCAR", "MAX", "MUHAMMAD", "LEO", "TYLER", "JOSEPH", "ARCHIE", "HENRY", "LUCAS", "MOHAMMED", "ALEXANDER",
			"DYLAN", "LOGAN", "ISAAC", "MASON", "BENJAMIN", "JAKE", "FINLEY", "HARRISON", "EDWARD", "JAYDEN", "FREDDIE", "ADAM", "ZACHARY", "SEBASTIAN", "RYAN",
			"LEWIS", "THEO", "LUKE", "HARLEY", "MATTHEW", "HARVEY", "TOBY", "LIAM", "CALLUM", "ARTHUR", "MICHAEL", "JENSON", "TOMMY", "NATHAN", "BOBBY",
			"CONNOR", "DAVID", "MOHAMMAD", "LUCA", "CHARLES", "KAI", "JAMIE", "ALEX", "BLAKE", "FRANKIE", "REUBEN", "AARON", "DEXTER", "JUDE", "LEON", "OLLIE",
			"STANLEY", "ELLIOT", "GABRIEL", "CAMERON", "OWEN", "LOUIE", "AIDEN", "LOUIS", "ELIJAH", "FINLAY", "RHYS", "CALEB", "EVAN", "FREDERICK", "HUGO",
			"KIAN", "SONNY", "SETH", "KAYDEN", "TAYLOR", "KYLE", "ELLIOTT", "ROBERT", "THEODORE", "BAILEY", "RORY", "ELLIS");
}
