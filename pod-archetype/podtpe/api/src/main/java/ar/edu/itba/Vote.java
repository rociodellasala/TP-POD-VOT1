package ar.edu.itba;

import java.io.Serializable;
import java.util.List;

import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;

public class Vote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;
	
	private Integer tableId;
	private Province province;
	private List<Party> ranking;
	
	public Vote(Integer tableId, Province province, List<Party> ranking) {
		super();
		this.tableId = tableId;
		this.province = province;
		this.ranking = ranking;
	}
	
	public Integer getTableId() {
		return tableId;
	}
	
	public Province getProvince() {
		return province;
	}
	
	public List<Party> getRanking() {
		return ranking;
	}
	
	
}