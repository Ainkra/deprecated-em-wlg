package client;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;

public class ConditionParser {
	public static boolean validConditions(Player perso, String req) {
		if(req == null || req.equals(""))
			return true;
		if(req.contains("BI"))
			return false;
		Jep jep = new Jep();
		req = req.replace("&", "&&").replace("=", "==").replace("|", "||").replace("!", "!=").replace("~", "==");
		if(req.contains("PO"))
			req = havePO(req, perso);
		if(req.contains("PN"))
			req = canPN(req, perso);
		try {
			jep.addVariable("PL", perso.getLevel());
			jep.addVariable("PS", perso.getSexe());
			jep.addVariable("PZ", 1);
			jep.addVariable("SI", perso.get_curCarte().get_id());
			jep.addVariable("MiS",perso.getId());
			jep.parse(req);
			Object result = jep.evaluate();
			boolean ok = false;
			if(result != null)
				ok = Boolean.parseBoolean(result.toString());
			return ok;
		} catch (JepException e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
		return true;
	}

	public static String havePO(String cond, Player perso) {
		boolean Jump = false;
		boolean ContainsPO = false;
		boolean CutFinalLenght = true;
		String copyCond = "";
		int finalLength = 0;
		if(cond.contains("&&")) {
			for(String cur : cond.split("&&")) {
				if(cond.contains("==")) {
					for(String cur2 : cur.split("==")) {
						if(cur2.contains("PO")) {
							ContainsPO = true;
							continue;
						}
						if(Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if(!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2+"==";
							Jump = true;
							continue;
						}
						if(cur2.contains("!=")) 
							continue;
						ContainsPO = false;
						if(perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
							copyCond += Integer.parseInt(cur2)+"=="+Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2)+"=="+0;
						}
					}
				}
				if(cond.contains("!=")) {
					for(String cur2 : cur.split("!=")) {
						if(cur2.contains("PO"))  {
							ContainsPO = true;
							continue;
						}
						if(Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if(!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2+"!=";
							Jump = true;
							continue;
						}
						if(cur2.contains("==")) 
							continue;
						ContainsPO = false;
						if(perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
							copyCond += Integer.parseInt(cur2)+"!="+Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2)+"!="+0;
						}
					}
				}
				copyCond += "&&";
			}
		} else if(cond.contains("||")) {
			for(String cur : cond.split("\\|\\|")) {
				if(cond.contains("==")) {
					for(String cur2 : cur.split("==")) {
						if(cur2.contains("PO"))  {
							ContainsPO = true;
							continue;
						}
						if(Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if(!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2+"==";
							Jump = true;
							continue;
						}
						if(cur2.contains("!=")) 
							continue;
						ContainsPO = false;
						if(perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
							copyCond += Integer.parseInt(cur2)+"=="+Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2)+"=="+0;
						}
					}
				}
				if(cond.contains("!=")) {
					for(String cur2 : cur.split("!=")) {
						if(cur2.contains("PO")) {
							ContainsPO = true;
							continue;
						}
						if(Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if(!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2+"!=";
							Jump = true;
							continue;
						}
						if(cur2.contains("==")) 
							continue;
						ContainsPO = false;
						if(perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
							copyCond += Integer.parseInt(cur2)+"!="+Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2)+"!="+0;
						}
					}
				}
				copyCond += "||";
			}
		} else {
			CutFinalLenght = false;
			if(cond.contains("==")) {
				for(String cur : cond.split("==")) {
					if(cur.contains("PO")) {
						continue;
					}
					if(cur.contains("!=")) 
						continue;
					if(perso.hasItemTemplate(Integer.parseInt(cur), 1)) {
						copyCond += Integer.parseInt(cur)+"=="+Integer.parseInt(cur);
					} else {
						copyCond += Integer.parseInt(cur)+"=="+0;
					}
				}
			}
			if(cond.contains("!=")) {
				for(String cur : cond.split("!=")) {
					if(cur.contains("PO"))  {
						continue;
					}
					if(cur.contains("=="))
						continue;
					if(perso.hasItemTemplate(Integer.parseInt(cur), 1)) {
						copyCond += Integer.parseInt(cur)+"!="+Integer.parseInt(cur);
					} else {
						copyCond += Integer.parseInt(cur)+"!="+0;
					}
				}
			}
		}
		if(CutFinalLenght) {
			finalLength = (copyCond.length()-2);
			copyCond = copyCond.substring(0, finalLength);
		}
		return copyCond;
	}

	public static String canPN(String cond, Player perso) {
		String copyCond = "";
		for(String cur : cond.split("==")) {
			if(cur.contains("PN")) {
				copyCond += "1==";
				continue;
			}
			if(perso.getName().toLowerCase().compareTo(cur) == 0) {
				copyCond += "1";
			} else {
				copyCond += "0";
			}
		}
		return copyCond;
	}
}