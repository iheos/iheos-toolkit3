package edu.tn.xds.metadata.editor.shared.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <b> This class represents an author.</b>
 * </p>
 * <p>
 * An author has the following parameters:
 * <ul>
 * <li>{@link #authorPerson}: The name of the author ( {@link String256}) ;</li>
 * <li>{@link #authorTelecommunications}: Telecommunication for the author, like email address ( {@link String256}) ;</li>
 * <li>{@link #authorInstitutions}: The institutions the author is working for
 * (List of {@link String256}) ;</li>
 * <li>{@link #authorRoles}: The roles of the author (List of {@link String256})
 * ;</li>
 * <li>{@link #authorSpecialties}: The specialties of the author (List of
 * {@link String256}).</li>
 * </ul>
 * </p>
 * <p>
 * It contains a toXML method to return the author in XML format.
 * </p>
 * <p/>
 * <p>
 * This class also contains getters/setters.<br>
 * In addition, it has several methods such as verify, toXML or toString.</br>
 * </p>
 * <p/>
 * <p>
 * <b>See below each method mentioned above.</b> <br>
 * {@link #verify() method verify}</br> {@link #toXML() method toXML} <br>
 * {@link #toString() method toString}
 * </p>
 *
 * @see DocumentModel class DocumentModel
 * @see ModelElement class ModelElement </p>
 */
public class Author implements ModelElement, Serializable {
    private static final long serialVersionUID = 4744955203293986600L;

    /**
     * <b>String256 authorPerson</b> - His name [Mandatory].<br>
     * Type: {@link String256}</br> </p>
     *
     * @see String256
     * @see Author
     */
    @NotNull
    private String256 authorPerson;

    /**
     * <b>List(String256) authorInstitutions</b> - The institutions that the
     * author is working for [Mandatory].<br>
     * Type: List of {@link String256}</br> </p>
     *
     * @see String256
     * @see Author
     */
    @NotNull
    @NotEmpty
    private List<String256> authorInstitutions;

    /**
     * <b>String256 authorRoles</b> - His roles [Mandatory].<br>
     * Type: List of {@link String256}</br> </p>
     *
     * @see String256
     * @see Author
     */
    @NotNull
    @NotEmpty
    private List<String256> authorRoles;

    /**
     * <b>List(String256) authorSpecialties</b> Specialties - his specialties
     * [Mandatory].<br>
     * Type: List of {@link String256}</br> </p>
     *
     * @see String256
     * @see Author
     */
    @NotNull
    @NotEmpty
    private List<String256> authorSpecialties;

    /**
     * <b>List(String256) authorTelecommunications</b> - His name [Mandatory].<br>
     * Type: {@link String256}</br> </p>
     *
     * @see String256
     * @see Author
     */
    @NotNull
    @NotEmpty
    private List<String256> authorTelecommunications;

    public Author() {
        authorPerson = new String256();
        authorInstitutions = new ArrayList<String256>();
        authorRoles = new ArrayList<String256>();
        authorSpecialties = new ArrayList<String256>();
        authorTelecommunications = new ArrayList<String256>();
    }

    public String256 getAuthorPerson() {
        return authorPerson;
    }

    public void setAuthorPerson(String256 authorPerson) {
        this.authorPerson = authorPerson;
    }

    public List<String256> getAuthorInstitutions() {
        return authorInstitutions;
    }

    public void setAuthorInstitutions(List<String256> authorInstitutions) {
        this.authorInstitutions = authorInstitutions;
    }

    public List<String256> getAuthorRoles() {
        return authorRoles;
    }

    public void setAuthorRoles(List<String256> authorRoles) {
        this.authorRoles = authorRoles;
    }

    public List<String256> getAuthorSpecialties() {
        return authorSpecialties;
    }

    public void setAuthorSpecialties(List<String256> authorSpecialties) {
        this.authorSpecialties = authorSpecialties;
    }

    /**
     * <p>
     * <b>Method toString</b> <br>
     * This method will be called to print author into a String </br>
     * </p>
     *
     * @return String which contains the author
     * @see Author class Author
     */
    @Override
    public String toString() {
        String answer;
        answer = "Author " + authorPerson.toString() + ", AuthorInstitutions=\"";

        for (String256 str : authorInstitutions) {
            answer = answer + str.toString() + ",";
        }
        answer = answer + "\", AuthorRoles=\"";

        for (String256 str : authorRoles) {
            answer = answer + str.toString() + ",";
        }
        answer = answer + "\", AuthorSpecialties=\"";

        for (String256 str : authorSpecialties) {
            answer = answer + str.toString() + ",";
        }
        answer = answer + "\"";

        return answer;
    }

    public List<String256> getAuthorTelecommunications() {
        return authorTelecommunications;
    }

    public void setAuthorTelecommunications(List<String256> authorTelecommunications) {
        this.authorTelecommunications = authorTelecommunications;
    }

    /**
     * <p>
     * <b>Method toXML</b> <br>
     * This method will be called to build a XML file by the
     * {@link DocumentModel} with the information taken from the local Author.
     * </br>
     * </p>
     *
     * @return String which contains the author in XML format
     * @see Author class Author
     */
    public String toXML() {
        String answer;
        answer = "\t\t<author>\n";

        answer += "\t\t\t<authorPerson>" + authorPerson.toString() + "</authorPerson>\n";

        answer += "\t\t\t<authorInstitutions>\n";
        for (String256 str : authorInstitutions) {
            answer = answer + "\t\t\t\t<authorInstitution>" + str.toString() + "</authorInstitution>\n";
        }
        answer = answer + "\t\t\t</authorInstitutions>\n";

        answer += "\t\t\t<authorRoles>\n";
        for (String256 str : authorRoles) {
            answer = answer + "\t\t\t\t<authorRole>" + str.toString() + "</authorRole>\n";
        }
        answer = answer + "\t\t\t</authorRoles>\n";

        answer += "\t\t\t<authorSpecialties>\n";
        for (String256 str : authorSpecialties) {
            answer = answer + "\t\t\t\t<authorSpecialty>" + str.toString() + "</authorSpecialty>\n";
        }
        answer = answer + "\t\t\t</authorSpecialties>\n";

        answer = answer + "\t\t\t<authorTelecommunications>\n";
        for (String256 str : authorTelecommunications) {
            answer = answer + "\t\t\t\t<authorTelecommunication>" + str.toString() + "</authorTelecommunication>\n";
        }
        answer = answer + "\t\t\t</authorTelecommunications>\n";

        answer += "\t\t</author>\n";

        return answer;
    }

    /**
     * <p>
     * <b>Method verify</b> <br>
     * This method will be called to check whether the syntax's Author is
     * correct </br>
     * </p>
     *
     * @return boolean true if the syntax is correct, else return false
     * @throws String256Exception if there is a String256 with more than 256 characters
     * @see Author class Author
     */
    @Override
    public boolean verify() throws String256Exception {
        boolean answer = true;
        answer = authorPerson.verify();
        for (String256 str256 : authorInstitutions) {
            answer = str256.verify();
        }

        for (String256 str256 : authorRoles) {
            answer = str256.verify();
        }

        for (String256 str256 : authorSpecialties) {
            answer = str256.verify();
        }
        for (String256 str256 : authorTelecommunications) {
            answer = str256.verify();
        }
        return answer;
    }
}
