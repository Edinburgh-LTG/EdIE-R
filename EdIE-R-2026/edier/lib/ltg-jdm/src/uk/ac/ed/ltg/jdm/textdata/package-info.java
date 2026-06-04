/**
 * Provides an object model for interacting with the XML documents used by the
 * NLP Pipeline (LTG-XML).  This model enables LTG-XML documents to be loaded and
 * saved in a consistent way that retains all the critical attributes of the
 * document and accurately maintains the text including white space.  It also
 * provides a range of methods for querying the data in the document and for
 * adding metadata.
 * 
 * <p>
 * Maintaining all the data in a LTG-XML document whilst making alterations to
 * attributes and metadata is quite a difficult task.  This package simplifies
 * this process.
 */
package uk.ac.ed.ltg.jdm.textdata;