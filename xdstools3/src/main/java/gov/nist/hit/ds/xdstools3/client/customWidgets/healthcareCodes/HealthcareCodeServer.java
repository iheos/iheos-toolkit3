package gov.nist.hit.ds.xdstools3.client.customWidgets.healthcareCodes;


import gov.nist.hit.ds.xdstools3.client.RESTUtils.OperationType;
import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointDSRequest;
import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointDSResponse;
import gov.nist.hit.ds.xdstools3.client.RESTUtils.DSResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Jersey resource for a SmartGWT {@link //MessageDS}.
 * <p>
 *
 * @see //http://docs.sun.com/app/docs/doc/820-4867/ggnxo?l=en&a=view
 * @see //http://blogs.sun.com/enterprisetechtips/entry/jersey_and_spring
 */

@Path("/healthcodes")
public class HealthcareCodeServer {
    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @POST
    @Path("/add")
    public EndpointDSResponse create(EndpointDSRequest request) {
        EndpointDSResponse response = new EndpointDSResponse();

        if (request.getOperationType() != OperationType.ADD || request.getMessages().size() != 1) {
            response.setStatus(DSResponse.STATUS_FAILURE);
        } else {
            //	Message message = request.getMessages().iterator().next();

            try {
                // create the message
                System.out.println("success on server");
                // save message here in Array
                //  response.addMessage(message);

                response.setStatus(DSResponse.STATUS_SUCCESS);
            } catch (Exception e) {
                response.setStatus(DSResponse.STATUS_FAILURE);
                e.printStackTrace();
            }
        }

        return response;
    }

    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @POST
    @Path("/update")
    public EndpointDSResponse update(EndpointDSRequest request) {
        EndpointDSResponse response = new EndpointDSResponse();

        if (request.getOperationType() != OperationType.UPDATE || request.getMessages().size() != 1) {
            response.setStatus(DSResponse.STATUS_FAILURE);
        } else {
            try {
                //Message data = (Message) request.getMessages().iterator().next();
                System.out.println("success on server");
                response.setStatus(DSResponse.STATUS_SUCCESS);
            } catch (Exception e) {
                response.setStatus(DSResponse.STATUS_FAILURE);
                e.printStackTrace();
            }
        }

        return response;
    }

    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @POST
    @Path("/remove")
    public EndpointDSResponse delete(EndpointDSRequest request) {
        EndpointDSResponse response = new EndpointDSResponse();

        if (request.getOperationType() != OperationType.REMOVE || request.getMessages().size() != 1) {
            response.setStatus(DSResponse.STATUS_FAILURE);
        } else {
            try {
                //Message data = request.getMessages().iterator().next();
                //message.remove();
                //response.addMessage(data);
                System.out.println("success on server");
                response.setStatus(DSResponse.STATUS_SUCCESS);
            } catch (Exception e) {
                response.setStatus(DSResponse.STATUS_FAILURE);
                e.printStackTrace();
            }
        }

        return response;
    }

    @POST
    @Produces( { MediaType.APPLICATION_XML})
    @Consumes( { MediaType.TEXT_XML })
    @Path("/read")
    public HealthcareCodeDSResponse read(HealthcareCodeDSRequest request) {
        HealthcareCodeDSResponse response = new HealthcareCodeDSResponse();
        response.setStatus(DSResponse.STATUS_SUCCESS);
//
//		response.setStartRow(request.getStartRow());
//
//        if (request.getOperationType() != OperationType.FETCH) {
//            response.setStatus(DSResponse.STATUS_FAILURE);
//        } else {
//            try {

        // enter fake data
        HealthcareCode hc1 = new HealthcareCode();
        hc1.setCode("code example");
        hc1.setDescription("This is the health code description.");
        response.addMessage(hc1);
        response.setEndRow(1);
        response.setTotalRows(1);



        // Collection<Endpoint> messages = MessageDSR(request.getStartRow(), 1 + (request.getEndRow() - request.getStartRow()));
////				long count = Message.countMessages();
////				response.setEndRow(response.getStartRow()+messages.size()-1);
////				response.setTotalRows((int)count);
////				for (Message message : messages) {
////					response.addMessage(message);
////				}
//               //response.addMessage(new Message(sb.toString()));



//
//                StringBuffer sb = new StringBuffer();
//                sb.append("<response>");
//                sb.append("<status>0</status>");
//                sb.append("<startRow>" + "0" + "</startRow>");
//                sb.append("<endRow>" + "1" + "</endRow>");
//                sb.append("<totalRows>10000</totalRows>");
//                sb.append("<data>");
//
//                sb.append("<record><id>idtest</id><value>Value</value></record>");
//                sb.append("</data>");
//                sb.append("</response>");
        System.out.println("success on server");



        return response;
//

//            } catch (Exception e) {
//                SC.say("Cannot reach server.");
//                response.setStatus(DSResponse.STATUS_FAILURE);
//            }
    }
    // return "";
    // }

//    @POST
//    @Produces( { MediaType.APPLICATION_XML})
//    @Consumes( { MediaType.TEXT_XML })
//    @Path("/read")
//    public MessageDSResponse read(MessageDSRequest request) {
//        MessageDSResponse response = new MessageDSResponse();
//
//        response.setStartRow(request.getStartRow());
//
//        if (request.getOperationType() != OperationType.FETCH) {
//            response.setStatus(DSResponse.STATUS_FAILURE);
//        } else {
//            try {
////				Collection<Message> messages = findMessageEntries(request.getStartRow(), 1 + (request.getEndRow() - request.getStartRow()));
////				long count = Message.countMessages();
////				response.setEndRow(response.getStartRow()+messages.size()-1);
////				response.setTotalRows((int)count);
////				for (Message message : messages) {
////					response.addMessage(message);
////				}
//                //response.addMessage(new Message(sb.toString()));
//
//                System.out.println("success on server");
//            } catch (Exception e) {
//                response.setStatus(DSResponse.STATUS_FAILURE);
//            }
//
//            response.setStatus(DSResponse.STATUS_SUCCESS);
//        }
//
//        return response;
//    }


}
