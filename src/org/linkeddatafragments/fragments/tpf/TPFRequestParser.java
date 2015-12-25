package org.linkeddatafragments.fragments.tpf;

import javax.servlet.http.HttpServletRequest;

import org.linkeddatafragments.config.ConfigReader;
import org.linkeddatafragments.fragments.FragmentRequestParserBase;
import org.linkeddatafragments.fragments.IFragmentRequestParser;
import org.linkeddatafragments.fragments.LinkedDataFragmentRequest;
import org.linkeddatafragments.util.TriplePatternElementParser;

/**
 * An {@link IFragmentRequestParser} for {@link TriplePatternFragmentRequest}s.
 *
 * @param <TermType> type for representing RDF terms in triple patterns 
 * @param <VarType> type for representing specific variables in triple patterns
 *
 * @author <a href="http://olafhartig.de">Olaf Hartig</a>
 */
public class TPFRequestParser<TermType,VarType>
    extends FragmentRequestParserBase
{
    public final TriplePatternElementParser<TermType,VarType> elmtParser;

    public TPFRequestParser(
                final TriplePatternElementParser<TermType,VarType> elmtParser )
    {
        this.elmtParser = elmtParser;
    }

    @Override
    protected Worker getWorker( final HttpServletRequest httpRequest,
                                final ConfigReader config )
                                               throws IllegalArgumentException
    {
        return new Worker( httpRequest, config );
    }

    protected class Worker extends FragmentRequestParserBase.Worker
    {   
        public Worker( final HttpServletRequest request,
                       final ConfigReader config )
        {
            super( request, config );
        }

        @Override
        public LinkedDataFragmentRequest createFragmentRequest()
                                               throws IllegalArgumentException
        {
            return new TriplePatternFragmentRequestImpl<TermType,VarType>(
                                                         getFragmentURL(),
                                                         getDatasetURL(),
                                                         pageNumberWasRequested,
                                                         pageNumber,
                                                         getSubject(),
                                                         getPredicate(),
                                                         getObject() );
        }

        public TriplePatternElement<TermType,VarType> getSubject() {
            return getParameterAsTriplePatternElement(
                    TriplePatternFragmentRequest.PARAMETERNAME_SUBJ );
        }

        public TriplePatternElement<TermType,VarType> getPredicate() {
            return getParameterAsTriplePatternElement(
                    TriplePatternFragmentRequest.PARAMETERNAME_PRED );
        }

        public TriplePatternElement<TermType,VarType> getObject() {
            return getParameterAsTriplePatternElement(
                    TriplePatternFragmentRequest.PARAMETERNAME_OBJ );
        }

        public TriplePatternElement<TermType,VarType>
                   getParameterAsTriplePatternElement( final String paramName )
        {
            final String parameter = request.getParameter( paramName );
            return elmtParser.parseIntoTriplePatternElement( parameter );
        }

    } // end of class Worker

}
