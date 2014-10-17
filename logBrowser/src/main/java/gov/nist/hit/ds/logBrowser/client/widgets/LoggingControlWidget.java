package gov.nist.hit.ds.logBrowser.client.widgets;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoggingControlWidget extends Composite {


    private SimpleEventBus eventBus;
    private String caption;

    // mouse positions relative to canvas
    int mouseX, mouseY;

    final CssColor redrawColor = CssColor.make("rgba(255,255,255,0.6)");
    Context2d context;
    Context2d frontContext;
    Canvas canvas;
    Canvas frontCanvas;


    double centerX = 200;
    double centerY = 200;
    double radius = 25;

    double oldMouseX = 0;
    double oldMouseY = 0;

    double oldMouseOrigX = 0;
    double oldMouseOrigY = 0;

    /**
     *
     * @param caption
     */
   public LoggingControlWidget(Canvas backBuffer, Canvas frontCanvas, SimpleEventBus eventBus, String caption) {
       this.canvas = backBuffer;
       this.frontCanvas = frontCanvas;
       this.eventBus = eventBus;
       this.caption = caption;

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


        context = canvas.getContext2d();

        context.beginPath();

        context.setFillStyle(CssColor.make("white")); //
            context.fillRect(0,0,300,50);
            context.fill();
        context.closePath();

        context.beginPath();
        context.setFillStyle(CssColor.make("black")); //
        context.fillText("x:" + mouseX + ",y:" + mouseY, 10, 10); 
        double relativeX = Math.abs(mouseX-centerX);
        double relativeY = Math.abs(mouseY-centerY);



        context.fillText("distance from circle center:" + relativeX  + ", " + relativeY, 10,20);
        double tan = Math.atan(relativeY / relativeX);

        context.fillText("deg: " + Math.toDegrees(tan) + " arctan: " + tan,10,30);
        double newY = radius * (Math.sin(tan));
        double newX = radius * (Math.cos(tan));
        context.fillText("newX: " +  newX + " newY: " + newY,10,40);


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

        context.fillText("newC: " + (newX) + " newY: " + (newY), 10, 50);
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





        CanvasGradient gradient = context.createLinearGradient(150, 150, 190, 236);
        gradient.addColorStop(0, "black"); //red
        gradient.addColorStop(1, "white"); //lightgreen
        context.beginPath();
        context.setFillStyle(gradient);
        context.arc(centerX, centerY, radius + 12, 0, Math.PI * 2.0, true);
        context.fill();
        context.closePath();

        context.beginPath();
        context.arc(centerX-2, centerY, radius+8, 0, Math.PI * 2.0, true);
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
        context.arc(centerX, centerY-1, radius+4, 0, Math.PI * 2.0,  true );
        context.stroke();
        context.closePath();


        // Outer knob
        context.beginPath();
//        context.setFillStyle(CssColor.make("white"));
        context.setStrokeStyle("gray");
        context.arc(centerX, centerY, radius+3, 0, Math.PI * 2.0,  true );
        context.stroke();
        context.closePath();

        // Pointer
        context.beginPath();
        context.setFillStyle(CssColor.make("red"));
        context.arc(newX, newY, 3, 0, Math.PI * 2.0,  true );
        context.fill();


        context.setFillStyle(CssColor.make("gray"));
        context.fillText("Errors",130,195);

        context.setFillStyle(CssColor.make("gray"));
        context.fillText("Warnings",180,156);

        context.setFillStyle(CssColor.make("gray"));
        context.fillText("Info",239,195);

        context.closePath();

        frontContext.drawImage(context.getCanvas(),0,0);

        oldMouseX = newX;
        oldMouseY = newY;

        oldMouseOrigX = mouseX;
        oldMouseOrigY = mouseY;

    }

    public void draw(Context2d context) {

    }
 
}
