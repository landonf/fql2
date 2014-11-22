/* The following code was generated by JFlex 1.4.3 on 11/22/14 3:16 PM */

/*
 * Generated on 11/22/14 3:16 PM
 */
package fql_lib.X;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * 
 */

public class FpqlTokenMaker extends AbstractJFlexCTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int EOL_COMMENT = 4;
  public static final int YYINITIAL = 0;
  public static final int MLC = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2, 2
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\17\1\7\1\0\1\17\1\15\22\0\1\17\1\24\1\14"+
    "\1\16\1\1\1\24\1\24\1\6\1\25\1\25\1\21\1\24\1\24"+
    "\1\67\1\23\1\20\1\4\3\4\4\4\2\3\1\36\1\24\1\15"+
    "\1\71\1\70\1\24\1\16\1\52\1\5\1\53\1\5\1\44\1\40"+
    "\2\1\1\46\2\1\1\41\1\1\1\47\1\42\2\1\1\45\1\50"+
    "\1\51\2\1\1\43\3\1\1\25\1\10\1\25\1\15\1\2\1\0"+
    "\1\54\1\13\1\57\1\56\1\35\1\32\1\62\1\26\1\33\2\1"+
    "\1\34\1\61\1\55\1\60\1\30\1\66\1\12\1\31\1\27\1\11"+
    "\1\65\1\37\1\1\1\64\1\63\1\22\1\15\1\22\1\24\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\3\0\2\1\1\2\1\3\2\1\1\4\1\5\1\1"+
    "\1\6\7\1\1\7\13\1\1\10\1\11\5\10\1\12"+
    "\3\10\1\0\1\13\2\1\2\4\1\14\1\15\1\16"+
    "\2\1\1\17\27\1\1\20\11\0\2\1\1\4\1\21"+
    "\1\4\22\1\11\0\2\1\1\4\15\1\2\0\1\22"+
    "\2\0\1\23\1\0\2\1\1\4\3\1\1\24\5\1"+
    "\5\0\1\1\1\4\23\1";

  private static int [] zzUnpackAction() {
    int [] result = new int[181];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\72\0\164\0\256\0\350\0\u0122\0\256\0\u015c"+
    "\0\u0196\0\u01d0\0\u020a\0\u0244\0\256\0\u027e\0\u02b8\0\u02f2"+
    "\0\u032c\0\u0366\0\u03a0\0\u03da\0\256\0\u0414\0\u044e\0\u0488"+
    "\0\u04c2\0\u04fc\0\u0536\0\u0570\0\u05aa\0\u05e4\0\u061e\0\u0658"+
    "\0\u0692\0\256\0\u06cc\0\u0706\0\u0740\0\u077a\0\u07b4\0\256"+
    "\0\u07ee\0\u0828\0\u0862\0\u089c\0\u08d6\0\u0910\0\u094a\0\u0984"+
    "\0\u09be\0\256\0\256\0\256\0\u09f8\0\u0a32\0\350\0\u0a6c"+
    "\0\u0aa6\0\u0ae0\0\u0b1a\0\u0b54\0\u0b8e\0\u0bc8\0\u0c02\0\u0c3c"+
    "\0\u0c76\0\u0cb0\0\u0cea\0\u0d24\0\u0d5e\0\u0d98\0\u0dd2\0\u0e0c"+
    "\0\u0e46\0\u0e80\0\u0eba\0\u0ef4\0\u0f2e\0\u0f68\0\256\0\u0fa2"+
    "\0\u0fdc\0\u1016\0\u1050\0\u108a\0\u10c4\0\u10fe\0\u1138\0\u1172"+
    "\0\u11ac\0\u11e6\0\u1220\0\256\0\u125a\0\u1294\0\u12ce\0\u1308"+
    "\0\u1342\0\u137c\0\u13b6\0\u13f0\0\u142a\0\u1464\0\u149e\0\u14d8"+
    "\0\u1512\0\u154c\0\u1586\0\u15c0\0\u15fa\0\u1634\0\u166e\0\u16a8"+
    "\0\u16e2\0\u171c\0\u1756\0\u1790\0\u17ca\0\u1804\0\u183e\0\u1878"+
    "\0\u18b2\0\u18ec\0\u1926\0\u1960\0\u199a\0\u19d4\0\u1a0e\0\u1a48"+
    "\0\u1a82\0\u1abc\0\u1af6\0\u1b30\0\u1b6a\0\u1ba4\0\u1bde\0\u1c18"+
    "\0\u1c52\0\u1c8c\0\u1cc6\0\u1d00\0\u1d3a\0\u1d74\0\u1dae\0\u1de8"+
    "\0\u1e22\0\u1e5c\0\u1e96\0\u1ed0\0\u1f0a\0\350\0\u1f44\0\u1f7e"+
    "\0\u1fb8\0\u1ff2\0\u202c\0\u2066\0\u1cc6\0\u20a0\0\u1d74\0\u20da"+
    "\0\u2114\0\u214e\0\u2188\0\u21c2\0\u21fc\0\u2236\0\u2270\0\u22aa"+
    "\0\u22e4\0\u231e\0\u2358\0\u2392\0\u23cc\0\u2406\0\u2440\0\u247a"+
    "\0\u24b4\0\u24ee\0\u2528\0\u2562\0\u259c";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[181];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\4\2\5\2\6\1\5\1\4\1\7\1\4\1\10"+
    "\1\11\1\5\1\12\2\4\1\13\1\14\1\4\1\15"+
    "\2\4\1\15\1\16\1\17\1\20\1\21\1\22\1\23"+
    "\1\5\1\24\1\25\1\26\1\27\5\5\1\30\5\5"+
    "\1\31\1\32\1\33\1\34\1\35\1\36\3\5\1\37"+
    "\1\5\1\40\1\4\1\25\7\41\1\42\11\41\1\43"+
    "\4\41\1\44\3\41\1\45\4\41\1\46\32\41\7\47"+
    "\1\50\16\47\1\51\3\47\1\52\4\47\1\53\32\47"+
    "\73\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\30\5\3\0\3\55\2\6\1\55\2\0\4\55\2\0"+
    "\1\55\7\0\10\55\1\0\30\55\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\16\5\1\56\11\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\7\5\1\57"+
    "\1\0\30\5\3\0\7\12\1\60\1\61\3\12\1\62"+
    "\55\12\17\0\1\13\72\0\1\63\1\64\51\0\5\5"+
    "\2\0\1\54\3\5\12\0\10\5\1\0\21\5\1\65"+
    "\6\5\4\0\5\5\2\0\1\54\1\5\1\66\1\5"+
    "\12\0\1\5\1\67\6\5\1\0\25\5\1\70\2\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\5\5\1\67"+
    "\2\5\1\0\15\5\1\71\12\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\5\5\1\72\1\5\1\73\1\0"+
    "\16\5\1\74\1\5\1\75\7\5\4\0\5\5\2\0"+
    "\1\54\1\5\1\76\1\5\12\0\3\5\1\77\1\67"+
    "\1\5\1\100\1\5\1\0\15\5\1\101\12\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\16\5"+
    "\1\102\11\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\17\5\1\103\7\5\1\104\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\1\105\7\5\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\2\5\1\106\25\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\10\5\1\107\17\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\3\5\1\67\4\5\1\0"+
    "\16\5\1\74\11\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\21\5\1\110\6\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\7\5\1\111\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\15\5\1\112\3\5\1\113\6\5\4\0\5\5\2\0"+
    "\1\54\1\5\1\67\1\5\12\0\10\5\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\15\5\1\114\12\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\15\5\1\115\3\5\1\116\6\5"+
    "\73\0\1\25\1\0\7\41\1\0\11\41\1\0\4\41"+
    "\1\0\3\41\1\0\4\41\1\0\32\41\20\0\1\117"+
    "\100\0\1\120\71\0\1\121\3\0\1\122\75\0\1\123"+
    "\32\0\7\47\1\0\16\47\1\0\3\47\1\0\4\47"+
    "\1\0\32\47\27\0\1\124\71\0\1\125\3\0\1\126"+
    "\75\0\1\127\43\0\1\130\60\0\6\55\2\0\4\55"+
    "\2\0\1\55\7\0\10\55\1\0\30\55\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\5\5\1\77\2\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\3\5\12\0\1\5"+
    "\1\131\4\5\1\132\1\5\1\0\30\5\3\0\10\60"+
    "\1\133\3\60\1\134\61\60\1\12\1\60\1\12\1\0"+
    "\1\12\1\135\3\12\12\60\1\12\2\60\1\12\22\60"+
    "\1\12\14\60\1\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\22\5\1\136\5\5\4\0\5\5\2\0"+
    "\1\54\1\137\2\5\12\0\10\5\1\0\15\5\1\140"+
    "\12\5\4\0\5\5\2\0\1\54\3\5\12\0\2\5"+
    "\1\137\5\5\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\5\5\1\35\2\5\1\0\30\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\23\5"+
    "\1\141\4\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\6\5\1\142\1\5\1\0\30\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\17\5\1\67\10\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\1\143\7\5"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\21\5\1\144\6\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\1\5\1\67\6\5\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\21\5\1\145\6\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\6\5\1\112\1\5\1\0\30\5\4\0\5\5"+
    "\2\0\1\54\1\5\1\67\1\5\12\0\3\5\1\146"+
    "\2\5\1\67\1\5\1\0\30\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\23\5\1\147\4\5"+
    "\4\0\5\5\2\0\1\54\1\150\2\5\12\0\10\5"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\7\5\1\151\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\10\5\1\0\3\5\1\152\24\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\11\5"+
    "\1\153\16\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\17\5\1\147\10\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\6\5\1\154\1\5\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\3\5\1\137"+
    "\4\5\1\0\30\5\4\0\5\5\2\0\1\54\1\5"+
    "\1\155\1\5\12\0\10\5\1\0\30\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\2\5\1\156\5\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\1\5\1\157\1\5"+
    "\12\0\10\5\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\5\5\1\74\2\5\1\0\30\5\32\0"+
    "\1\160\72\0\1\161\75\0\1\162\74\0\1\163\61\0"+
    "\1\164\72\0\1\165\75\0\1\166\74\0\1\167\35\0"+
    "\3\170\5\0\1\170\16\0\1\170\2\0\1\170\2\0"+
    "\1\170\3\0\1\170\5\0\3\170\1\0\2\170\13\0"+
    "\5\5\2\0\1\54\1\171\2\5\12\0\10\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\3\5\12\0\10\5"+
    "\1\0\15\5\1\172\12\5\3\0\7\60\1\0\65\60"+
    "\3\173\2\60\1\133\2\60\1\173\1\134\15\60\1\173"+
    "\2\60\1\173\2\60\1\173\3\60\1\173\5\60\3\173"+
    "\1\60\2\173\12\60\1\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\21\5\1\174\6\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\7\5\1\67\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\16\5\1\175\11\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\22\5\1\176\5\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\7\5\1\177\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\7\5\1\141"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\22\5\1\67\5\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\1\200\27\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\1\5\1\201\6\5"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\7\5\1\202\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\10\5\1\0\15\5\1\203\12\5\4\0"+
    "\5\5\2\0\1\54\1\5\1\137\1\5\12\0\10\5"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\4\5\1\204\23\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\12\5\1\205\15\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\1\5\1\176"+
    "\6\5\1\0\30\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\7\5\1\206\1\0\30\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\2\5\1\207\5\5\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\5\5\1\210"+
    "\2\5\1\0\30\5\33\0\1\211\77\0\1\212\70\0"+
    "\1\161\57\0\1\213\76\0\1\214\77\0\1\215\70\0"+
    "\1\165\57\0\1\216\51\0\3\217\5\0\1\217\16\0"+
    "\1\217\2\0\1\217\2\0\1\217\3\0\1\217\5\0"+
    "\3\217\1\0\2\217\13\0\5\5\2\0\1\54\1\5"+
    "\1\220\1\5\12\0\10\5\1\0\30\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\1\5\1\221\6\5\1\0"+
    "\30\5\3\0\3\60\3\222\2\60\1\133\2\60\1\222"+
    "\1\134\15\60\1\222\2\60\1\222\2\60\1\222\3\60"+
    "\1\222\5\60\3\222\1\60\2\222\12\60\1\0\5\5"+
    "\2\0\1\54\3\5\12\0\10\5\1\0\22\5\1\223"+
    "\5\5\4\0\5\5\2\0\1\54\3\5\12\0\3\5"+
    "\1\224\4\5\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\10\5\1\0\15\5\1\67\12\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\20\5"+
    "\1\77\7\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\7\5\1\35\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\10\5\1\0\15\5\1\225\12\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\3\5\1\226\4\5"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\1\5\1\227\6\5\1\0\30\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\5\5\1\230\22\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\13\5\1\231\14\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\1\5\1\131\6\5\1\0\30\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\5\5\1\232\2\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\3\5\12\0\10\5"+
    "\1\0\15\5\1\233\12\5\34\0\1\161\4\0\1\212"+
    "\53\0\1\234\52\0\1\213\1\235\3\213\1\235\2\0"+
    "\3\213\2\0\1\235\1\0\1\213\1\235\1\0\3\235"+
    "\10\213\1\235\30\213\1\235\1\0\1\235\31\0\1\165"+
    "\4\0\1\215\53\0\1\236\52\0\1\216\1\237\3\216"+
    "\1\237\2\0\3\216\2\0\1\237\1\0\1\216\1\237"+
    "\1\0\3\237\10\216\1\237\30\216\1\237\1\0\1\237"+
    "\3\0\3\240\5\0\1\240\16\0\1\240\2\0\1\240"+
    "\2\0\1\240\3\0\1\240\5\0\3\240\1\0\2\240"+
    "\13\0\5\5\2\0\1\54\3\5\12\0\10\5\1\0"+
    "\16\5\1\67\11\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\5\5\1\241\2\5\1\0\30\5\3\0\3\60"+
    "\3\242\2\60\1\133\2\60\1\242\1\134\15\60\1\242"+
    "\2\60\1\242\2\60\1\242\3\60\1\242\5\60\3\242"+
    "\1\60\2\242\12\60\1\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\21\5\1\243\6\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\4\5\1\244\3\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\3\5\12\0\10\5"+
    "\1\0\16\5\1\245\11\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\5\5\1\246\2\5\1\0\30\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\6\5"+
    "\1\67\21\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\10\5\1\247\17\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\16\5\1\250\11\5"+
    "\4\0\5\5\2\0\1\54\2\5\1\251\12\0\10\5"+
    "\1\0\30\5\23\0\1\213\71\0\1\216\54\0\3\5"+
    "\5\0\1\5\16\0\1\5\2\0\1\5\2\0\1\5"+
    "\3\0\1\5\5\0\3\5\1\0\2\5\13\0\5\5"+
    "\2\0\1\54\3\5\12\0\10\5\1\0\21\5\1\252"+
    "\6\5\3\0\3\60\3\12\2\60\1\133\2\60\1\12"+
    "\1\134\15\60\1\12\2\60\1\12\2\60\1\12\3\60"+
    "\1\12\5\60\3\12\1\60\2\12\12\60\1\0\5\5"+
    "\2\0\1\54\1\5\1\253\1\5\12\0\10\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\3\5\12\0\10\5"+
    "\1\0\21\5\1\254\6\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\10\5\1\0\20\5\1\137\7\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\21\5"+
    "\1\255\6\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\14\5\1\256\13\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\10\5\1\0\23\5\1\67\4\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\6\5\1\147"+
    "\1\5\1\0\30\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\16\5\1\257\11\5\4\0\5\5"+
    "\2\0\1\54\3\5\12\0\2\5\1\260\5\5\1\0"+
    "\30\5\4\0\5\5\2\0\1\54\1\5\1\144\1\5"+
    "\12\0\10\5\1\0\30\5\4\0\5\5\2\0\1\54"+
    "\3\5\12\0\10\5\1\0\16\5\1\202\11\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\10\5\1\0\5\5"+
    "\1\67\22\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\10\5\1\0\15\5\1\261\12\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\1\262\7\5\1\0\30\5\4\0"+
    "\5\5\2\0\1\54\3\5\12\0\6\5\1\263\1\5"+
    "\1\0\30\5\4\0\5\5\2\0\1\54\3\5\12\0"+
    "\5\5\1\264\2\5\1\0\30\5\4\0\5\5\2\0"+
    "\1\54\3\5\12\0\5\5\1\265\2\5\1\0\30\5"+
    "\4\0\5\5\2\0\1\54\3\5\12\0\3\5\1\144"+
    "\4\5\1\0\30\5\4\0\5\5\2\0\1\54\3\5"+
    "\12\0\10\5\1\0\24\5\1\137\3\5\3\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[9686];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\3\0\1\11\2\1\1\11\5\1\1\11\7\1\1\11"+
    "\14\1\1\11\5\1\1\11\3\1\1\0\5\1\3\11"+
    "\32\1\1\11\11\0\3\1\1\11\23\1\11\0\20\1"+
    "\2\0\1\1\2\0\1\1\1\0\14\1\5\0\25\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[181];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public FpqlTokenMaker() {
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addHyperlinkToken(int, int, int)
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, false);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *        occurs.
	 * @param hyperlink Whether this token is a hyperlink.
	 */
	public void addToken(char[] array, int start, int end, int tokenType,
						int startOffset, boolean hyperlink) {
		super.addToken(array, start,end, tokenType, startOffset, hyperlink);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * Returns the text to place at the beginning and end of a
	 * line to "comment" it in a this programming language.
	 *
	 * @return The start and end strings to add to a line to "comment"
	 *         it out.
	 */
	public String[] getLineCommentStartAndEnd() {
		return new String[] { "//", null };
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
						case Token.COMMENT_MULTILINE:
				state = MLC;
				start = text.offset;
				break;

			/* No documentation comments */
			default:
				state = Token.NULL;
		}

		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream 
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream 
	 */
	public final void yyreset(Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtBOL  = true;
		zzAtEOF  = false;
	}




  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public FpqlTokenMaker(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public FpqlTokenMaker(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 180) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 3: 
          { addNullToken(); return firstToken;
          }
        case 21: break;
        case 14: 
          { start = zzMarkedPos-2; yybegin(MLC);
          }
        case 22: break;
        case 5: 
          { addToken(Token.WHITESPACE);
          }
        case 23: break;
        case 17: 
          { addToken(Token.ERROR_STRING_DOUBLE);
          }
        case 24: break;
        case 15: 
          { addToken(Token.RESERVED_WORD);
          }
        case 25: break;
        case 6: 
          { addToken(Token.SEPARATOR);
          }
        case 26: break;
        case 1: 
          { addToken(Token.IDENTIFIER);
          }
        case 27: break;
        case 10: 
          { addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken;
          }
        case 28: break;
        case 13: 
          { start = zzMarkedPos-2; yybegin(EOL_COMMENT);
          }
        case 29: break;
        case 4: 
          { addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken;
          }
        case 30: break;
        case 16: 
          { yybegin(YYINITIAL); addToken(start,zzStartRead+2-1, Token.COMMENT_MULTILINE);
          }
        case 31: break;
        case 12: 
          { addToken(Token.LITERAL_STRING_DOUBLE_QUOTE);
          }
        case 32: break;
        case 19: 
          { int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_EOL); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_EOL); start = zzMarkedPos;
          }
        case 33: break;
        case 18: 
          { int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_MULTILINE); start = zzMarkedPos;
          }
        case 34: break;
        case 20: 
          { addToken(Token.RESERVED_WORD_2);
          }
        case 35: break;
        case 11: 
          { addToken(Token.ERROR_NUMBER_FORMAT);
          }
        case 36: break;
        case 2: 
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
          }
        case 37: break;
        case 7: 
          { addToken(Token.OPERATOR);
          }
        case 38: break;
        case 8: 
          { 
          }
        case 39: break;
        case 9: 
          { addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken;
          }
        case 40: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case EOL_COMMENT: {
              addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken;
            }
            case 182: break;
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 183: break;
            case MLC: {
              addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken;
            }
            case 184: break;
            default:
            return null;
            }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
