/* C++ code produced by gperf version 3.0.3 */
/* Command-line: /Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/gperf -L C++ -E -t /private/var/folders/mv/yhmzb0s54zscv22_sts5dtvh0000gn/T/sunfurong/androidservice-generated/KrollGeneratedBindings.gperf  */
/* Computed positions: -k'' */

#line 3 "/private/var/folders/mv/yhmzb0s54zscv22_sts5dtvh0000gn/T/sunfurong/androidservice-generated/KrollGeneratedBindings.gperf"


#include <string.h>
#include <v8.h>
#include <KrollBindings.h>

#include "cn.named.service.AndroidserviceModule.h"


#line 13 "/private/var/folders/mv/yhmzb0s54zscv22_sts5dtvh0000gn/T/sunfurong/androidservice-generated/KrollGeneratedBindings.gperf"
struct titanium::bindings::BindEntry;
/* maximum key range = 1, duplicates = 0 */

class AndroidserviceBindings
{
private:
  static inline unsigned int hash (const char *str, unsigned int len);
public:
  static struct titanium::bindings::BindEntry *lookupGeneratedInit (const char *str, unsigned int len);
};

inline /*ARGSUSED*/
unsigned int
AndroidserviceBindings::hash (register const char *str, register unsigned int len)
{
  return len;
}

struct titanium::bindings::BindEntry *
AndroidserviceBindings::lookupGeneratedInit (register const char *str, register unsigned int len)
{
  enum
    {
      TOTAL_KEYWORDS = 1,
      MIN_WORD_LENGTH = 37,
      MAX_WORD_LENGTH = 37,
      MIN_HASH_VALUE = 37,
      MAX_HASH_VALUE = 37
    };

  static struct titanium::bindings::BindEntry wordlist[] =
    {
      {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""},
      {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""},
      {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""},
      {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""},
      {""},
#line 15 "/private/var/folders/mv/yhmzb0s54zscv22_sts5dtvh0000gn/T/sunfurong/androidservice-generated/KrollGeneratedBindings.gperf"
      {"cn.named.service.AndroidserviceModule", ::cn::named::service::AndroidserviceModule::bindProxy, ::cn::named::service::AndroidserviceModule::dispose}
    };

  if (len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH)
    {
      unsigned int key = hash (str, len);

      if (key <= MAX_HASH_VALUE)
        {
          register const char *s = wordlist[key].name;

          if (*str == *s && !strcmp (str + 1, s + 1))
            return &wordlist[key];
        }
    }
  return 0;
}
#line 16 "/private/var/folders/mv/yhmzb0s54zscv22_sts5dtvh0000gn/T/sunfurong/androidservice-generated/KrollGeneratedBindings.gperf"

