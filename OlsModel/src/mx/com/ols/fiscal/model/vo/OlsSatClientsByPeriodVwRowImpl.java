package mx.com.ols.fiscal.model.vo;

import java.math.BigDecimal;

import oracle.jbo.domain.RowID;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Apr 24 13:06:25 CDT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class OlsSatClientsByPeriodVwRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        RowId1,
        IndYearPrm,
        IndMonthPrm,
        IdClientSat,
        IndRfc,
        IndCompany,
        Alleged,
        Definitive,
        Unfulfilled;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static final int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }
    public static final int ROWID1 = AttributesEnum.RowId1.index();
    public static final int INDYEARPRM = AttributesEnum.IndYearPrm.index();
    public static final int INDMONTHPRM = AttributesEnum.IndMonthPrm.index();
    public static final int IDCLIENTSAT = AttributesEnum.IdClientSat.index();
    public static final int INDRFC = AttributesEnum.IndRfc.index();
    public static final int INDCOMPANY = AttributesEnum.IndCompany.index();
    public static final int ALLEGED = AttributesEnum.Alleged.index();
    public static final int DEFINITIVE = AttributesEnum.Definitive.index();
    public static final int UNFULFILLED = AttributesEnum.Unfulfilled.index();

    /**
     * This is the default constructor (do not remove).
     */
    public OlsSatClientsByPeriodVwRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute RowId1.
     * @return the RowId1
     */
    public RowID getRowId1() {
        return (RowID) getAttributeInternal(ROWID1);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute RowId1.
     * @param value value to set the  RowId1
     */
    public void setRowId1(RowID value) {
        setAttributeInternal(ROWID1, value);
    }

    /**
     * Gets the attribute value for the calculated attribute IndYearPrm.
     * @return the IndYearPrm
     */
    public String getIndYearPrm() {
        return (String) getAttributeInternal(INDYEARPRM);
    }

    /**
     * Gets the attribute value for the calculated attribute IndMonthPrm.
     * @return the IndMonthPrm
     */
    public String getIndMonthPrm() {
        return (String) getAttributeInternal(INDMONTHPRM);
    }

    /**
     * Gets the attribute value for the calculated attribute IdClientSat.
     * @return the IdClientSat
     */
    public BigDecimal getIdClientSat() {
        return (BigDecimal) getAttributeInternal(IDCLIENTSAT);
    }

    /**
     * Gets the attribute value for the calculated attribute IndRfc.
     * @return the IndRfc
     */
    public String getIndRfc() {
        return (String) getAttributeInternal(INDRFC);
    }

    /**
     * Gets the attribute value for the calculated attribute IndCompany.
     * @return the IndCompany
     */
    public String getIndCompany() {
        return (String) getAttributeInternal(INDCOMPANY);
    }

    /**
     * Gets the attribute value for the calculated attribute Alleged.
     * @return the Alleged
     */
    public BigDecimal getAlleged() {
        return (BigDecimal) getAttributeInternal(ALLEGED);
    }

    /**
     * Gets the attribute value for the calculated attribute Definitive.
     * @return the Definitive
     */
    public BigDecimal getDefinitive() {
        return (BigDecimal) getAttributeInternal(DEFINITIVE);
    }

    /**
     * Gets the attribute value for the calculated attribute Unfulfilled.
     * @return the Unfulfilled
     */
    public BigDecimal getUnfulfilled() {
        return (BigDecimal) getAttributeInternal(UNFULFILLED);
    }
}
