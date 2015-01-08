package gov.nist.hit.ds.repository.ui.client.widgets;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.hit.ds.repository.ui.client.event.reportingLevel.ReportingLevelUpdatedEvent;

import java.util.ArrayList;

public class LoggingControlWidget extends Composite {


    private EventBus eventBus;
    private String caption;

    private ArrayList<String> statusCodes = new ArrayList<String>();

    // mouse positions relative to canvas
    int mouseX, mouseY;

    final CssColor redrawColor = CssColor.make("rgba(255,255,255,0.6)");
    Context2d context;
    Context2d frontContext;
    Canvas canvas;
    Canvas frontCanvas;

    double hostWidth;
    double hostHeight;

    double centerX = 200;
    double centerY = 200;
    double radius = 25;
    double circleRadius = radius;

    double oldMouseX = 0;
    double oldMouseY = 0;

    double oldMouseOrigX = 0;
    double oldMouseOrigY = 0;

    String flag ="";
    String previousFlag = "";


    boolean debug = false; // Use 400x400 host area

    /**
     *
     * @param caption
     */
   public LoggingControlWidget(Canvas backBuffer, Canvas frontCanvas, EventBus eventBus, String caption, double hostWidth, double hostHeight) {
       this.canvas = backBuffer;
       this.frontCanvas = frontCanvas;
       this.eventBus = eventBus;
       this.caption = caption;

       this.hostHeight = hostHeight;
       this.hostWidth = hostWidth;

       centerX = this.hostHeight / 2;
       centerY = this.hostWidth / 2;

       radius = .17D * hostWidth;
       circleRadius = radius+(.12D*radius);

     // All composites must call initWidget() in their constructors.
     initWidget(initCanvas());

   }







    private Widget initCanvas(){


//        RootPanel.get(canvasHolderId).add(canvas);


//        context = canvas.getContext2d();
//
//        context.beginPath();
//        context.setFillStyle(CssColor.make("gray")); //
//
//
//        context.arc(centerX+1, centerY-1, radius+2, 0, Math.PI * 2.0,  true );
//
//
//        context.stroke();
//
//        context.closePath();
//
//
//        return frontCanvas;

        frontContext = frontCanvas.getContext2d();

//        frontContext.beginPath();
//        frontContext.setFillStyle(CssColor.make("gray"));
//
//        frontContext.arc(centerX+1, centerY-1, radius+2, 0, Math.PI * 2.0,  true );
//
//        frontContext.stroke();
//
//        frontContext.closePath();


        return frontCanvas;

    }

    public void update(double mouseX, double mouseY) {

        try {
        context = canvas.getContext2d();

        context.beginPath();

        context.setFillStyle(CssColor.make("white")); //
            context.fillRect(0,0,hostWidth,hostHeight);
            context.fill();
        context.closePath();

        context.beginPath();
        context.setFillStyle(CssColor.make("black")); //


        // Control restriction beyond lower half of the control height
        double bottomHalfOfControl = hostHeight / 2;
        if (mouseY> bottomHalfOfControl) { // Check status and return here to avoid flipping control when under the bottom control area 195.0D@400pxhost
            mouseY= bottomHalfOfControl;
        }

        fillText(context, "before x:" + mouseX + ",y:" +  mouseY, 150,10);

        // Forward middle range to next status

        // Forward to WARNING (more than 45 degrees)
        double widgetCenter = hostWidth / 2;

        boolean mouseUp = (mouseX==-1 && mouseY==-1);
        boolean initialState = (mouseX==0 && mouseY==0);

        if (mouseX>0 && mouseY>0) {
            double leg45 = (circleRadius * Math.sin(45));
            if (!"A".equals(flag) && mouseX> (widgetCenter - leg45) && mouseX<widgetCenter) { // 45 degrees 179.0D 200.0D @400sq. px host - Snap to WARNING
                flag="A";
            } else if (!"B".equals(flag) && mouseX>widgetCenter + leg45 && (mouseY > widgetCenter-leg45)) { // Snap to WARNING->INFO 223
                flag="B";

            } else if (!"C".equals(flag) && mouseX<(widgetCenter+leg45) && mouseX>widgetCenter) { // Snap to WARNING<-INFO
                flag="C";

            } else if (!"D".equals(flag) && mouseX<(widgetCenter+leg45) && (mouseY > widgetCenter-leg45)) { // Snap to ERROR 179
                flag="D";
//                mouseX=widgetCenter-circleRadius;
//                mouseY=widgetCenter;
            }

        } else if (mouseUp) {
            if ("D".equals(flag)) {
                mouseX=widgetCenter-circleRadius;
                mouseY=widgetCenter;
            } else if ("C".equals(flag)) {
                mouseX = widgetCenter;
            } else if ("B".equals(flag)) {
                mouseX=widgetCenter + circleRadius; // 287
                mouseY=widgetCenter;
            } else if ("A".equals(flag)) {
                mouseX = widgetCenter;
            }
        }


        fillText(context, flag +  " x:" + mouseX + ",y:" +  mouseY, 10,10);


        double relativeX = Math.abs(mouseX-centerX);
        double relativeY = Math.abs(mouseY-centerY);




        fillText(context, "distance from circle center:" + relativeX + ", " + relativeY, 10, 20);
        double tan = Math.atan(relativeY / relativeX);

        fillText(context,"deg: " + Math.toDegrees(tan) + " arctan: " + tan,10,30);
        double newY = radius * (Math.sin(tan));
        double newX = radius * (Math.cos(tan));
        fillText(context,"newX: " +  newX + " newY: " + newY,10,40);

        // Range restriction
        // 165,200 = ERROR
        // 244,200 = INFO

        // Initial setting = ERRORS
        if (initialState) {
            flag = "D";
            newX = radius;
            newY = 0.0D;
        }


        // Q1
         if (mouseX>=centerX && mouseY<=centerY) {
             newX += centerX;
             newY = (centerY-newY);
         } else if (mouseX <= centerX && mouseY <= centerY) {

          // Q2
                newX = centerX-newX;
                newY = centerY-newY;

        } else if (mouseX <= centerX && mouseY >= centerY) {
          // Q3

             newX = centerX-newX;
             newY = centerY+newY;


         } else if (mouseX >= centerX && mouseY >= centerY) {
             // Q4

             newX += centerX;
             newY += centerY;
         }



        fillText(context,"newC: " + (newX) + " newY: " + (newY), 10, 50);
//        context.closePath();

//        frontContext.drawImage(context.getCanvas(),0,0);
//
//        context = canvas.getContext2d();
//
//        context.beginPath();


//        context.setFillStyle(CssColor.make("white")); //
//        context.setFont("bold 8px");
//        context.fillText(".",oldMouseX,oldMouseY);
//        context.fillRect(oldMouseX,oldMouseY,2,2);


//        context.setFillStyle(CssColor.make("red")); //
//        context.setFont("normal 8px");
//        context.fillText(".",newX,newY);


        context.closePath();

        context.beginPath();
        context.setFillStyle(CssColor.make("white")); //
        context.fillRect(166,166,100,100);
        context.fill();
        context.closePath();



//        context.setFillStyle(CssColor.make("white")); //
//        context.arc(oldMouseX, oldMouseY, 2, 0, Math.PI * 2.0,  true );
//        context.setFillStyle(CssColor.make("red")); //





        CanvasGradient gradient = context.createLinearGradient(.375D * hostWidth, .375D * hostHeight, .475D * hostWidth, .59D * hostHeight);
        gradient.addColorStop(0, "gray"); //red
        gradient.addColorStop(1, "white"); //lightgreen

        context.beginPath();
        context.setFillStyle(gradient);
        context.arc(centerX, centerY, radius + (.5*radius), 0, Math.PI * 2.0, true);
        context.fill();
        context.closePath();

        context.beginPath();
        context.arc(centerX-2, centerY, radius+(.32*radius), 0, Math.PI * 2.0, true);
        context.setFillStyle("white");
        context.fill();
        context.closePath();


        context.beginPath();
        context.setFillStyle(CssColor.make("white")); //
        context.fillRect(centerX-50,centerY,100,100);
        context.fill();
        context.closePath();




        // Knob shading depth
        context.beginPath();
        context.arc(centerX, centerY+1, radius+(radius*.075D), 0, Math.PI * 2.0,  true );
        context.stroke();
        context.closePath();


        // Outer knob
        context.beginPath();
//        context.setFillStyle(CssColor.make("white"));
        context.setStrokeStyle("gray");
        context.arc(centerX, centerY, circleRadius, 0, Math.PI * 2.0,  true );
        context.stroke();
        context.closePath();

        // Pointer
        context.beginPath();
        context.setFillStyle(CssColor.make("red"));
        context.arc(newX, newY, 2.5D, 0, Math.PI * 2.0,  true);
        context.fill();



        String warningLabelColor = "black";
        String errorLabelColor = "black";
        String infoLabelColor = "black";




//        if (!mouseUp) {


            statusCodes.clear();

            if ( "A".equals(flag) || "C".equals(flag)) {
                errorLabelColor = "red";
                warningLabelColor = "orange";

                statusCodes.add("ERROR");
                statusCodes.add("WARNING");
            } else if ("D".equals(flag)) {
                errorLabelColor = "red";

                statusCodes.add("ERROR");
            } else if ("B".equals(flag)) {
                errorLabelColor = "red";
                warningLabelColor = "orange";
                infoLabelColor = "green";

                statusCodes.add("ERROR");
                statusCodes.add("WARNING");
                statusCodes.add("INFO");
            }
//        }


        context.setFont("normal 6px");
        context.setFillStyle(CssColor.make(errorLabelColor));
        context.fillText("Error",0 ,widgetCenter); // Y=195


        context.setFillStyle(CssColor.make(warningLabelColor));
        context.fillText("Warning",.28D*hostWidth,.22D*hostHeight);

        context.setFillStyle(CssColor.make(infoLabelColor));
        context.fillText("Info",.75D*hostWidth,widgetCenter); // Y=195

        context.setFillStyle(CssColor.make("black"));
        context.setFont("bold 6px");
        context.fillText("Reporting Level",.10D*hostWidth,.8D*hostHeight);


        context.closePath();

        frontContext.drawImage(context.getCanvas(),0,0);

        oldMouseX = newX;
        oldMouseY = newY;

        oldMouseOrigX = mouseX;
        oldMouseOrigY = mouseY;


            // Use this only in 1000 ms refresh mode to debug
//            System.out.println(">>>>>>>>>>>>>> previousFlag: " + previousFlag + " flag: " + flag + " initialState: " + initialState + " mouseUp: " + mouseUp);
        if ((initialState || mouseUp) && (!previousFlag.equals(flag))) {
            eventBus.fireEvent(new ReportingLevelUpdatedEvent(statusCodes));
            previousFlag = flag;
        }



        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private void fillText(Context2d ctx, String text, double x, double y) {
        if (debug) {
            ctx.fillText(text, x, y);
        }
    }

    public void draw(Context2d context) {

    }


    public ArrayList<String> getStatusCodes() {
        return statusCodes;
    }

    public void setStatusCodes(ArrayList<String> statusCodes) {
        this.statusCodes = statusCodes;
    }
}
