package gov.nist.toolkit.wsseTool.util;

import gov.nist.hit.ds.wsseTool.util.NamespaceContextMap;
import gov.nist.toolkit.wsseTool.BaseTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.junit.Assert;
import org.junit.Test;

//JUnit 4 test
public class NamespaceContextMapTest extends BaseTest {

 @Test
 public void testContext() {
   Map<String, String> mappings = new HashMap<String, String>();
   mappings.put("foo", "http://foo");
   mappings.put("altfoo", "http://foo");
   mappings.put("bar", "http://bar");
   mappings.put(XMLConstants.XML_NS_PREFIX,
       XMLConstants.XML_NS_URI);

   NamespaceContext context = new NamespaceContextMap(
       mappings);
   for (Map.Entry<String, String> entry : mappings
       .entrySet()) {
     String prefix = entry.getKey();
     String namespaceURI = entry.getValue();

     Assert.assertEquals("namespaceURI", namespaceURI,
         context.getNamespaceURI(prefix));
     boolean found = false;
     Iterator<?> prefixes = context
         .getPrefixes(namespaceURI);
     while (prefixes.hasNext()) {
       if (prefix.equals(prefixes.next())) {
         found = true;
         break;
       }
       try {
         prefixes.remove();
         Assert.fail("rw");
       } catch (UnsupportedOperationException e) {
       }
     }
     Assert.assertTrue("prefix: " + prefix, found);
     Assert.assertNotNull("prefix: " + prefix, context
         .getPrefix(namespaceURI));
   }

   Map<String, String> ctxtMap = ((NamespaceContextMap) context)
       .getMap();
   for (Map.Entry<String, String> entry : mappings
       .entrySet()) {
     Assert.assertEquals(entry.getValue(), ctxtMap
         .get(entry.getKey()));
   }

   System.out.println(context.toString());
 }

 @Test
 public void testModify() {
   NamespaceContextMap context = new NamespaceContextMap();

   try {
     Map<String, String> ctxtMap = context.getMap();
     ctxtMap.put("a", "b");
     Assert.fail("rw");
   } catch (UnsupportedOperationException e) {
   }

   try {
     Iterator<String> it = context
         .getPrefixes(XMLConstants.XML_NS_URI);
     it.next();
     it.remove();
     Assert.fail("rw");
   } catch (UnsupportedOperationException e) {
   }
 }

 @Test
 public void testConstants() {
   NamespaceContext context = new NamespaceContextMap();
   Assert.assertEquals(XMLConstants.XML_NS_URI, context
       .getNamespaceURI(XMLConstants.XML_NS_PREFIX));
   Assert.assertEquals(
       XMLConstants.XMLNS_ATTRIBUTE_NS_URI, context
           .getNamespaceURI(XMLConstants.XMLNS_ATTRIBUTE));
   Assert.assertEquals(XMLConstants.XML_NS_PREFIX, context
       .getPrefix(XMLConstants.XML_NS_URI));
   Assert.assertEquals(
       XMLConstants.XMLNS_ATTRIBUTE_NS_URI, context
           .getNamespaceURI(XMLConstants.XMLNS_ATTRIBUTE));
 }

}
