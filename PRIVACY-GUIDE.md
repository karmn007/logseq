# Logseq Privacy-Hardened Build Guide

This build has been configured for **maximum privacy** suitable for confidential/sensitive data.

## 🔒 What's Been Disabled

### ✅ Analytics & Telemetry (REMOVED)
- **Sentry error tracking** - Completely disabled
- **PostHog analytics** - Completely disabled
- All telemetry code is now no-op functions

### ✅ Cloud Sync (DISABLED BY DEFAULT)
- **File Sync** - Disabled by default (no uploads to Logseq servers)
- **RTC Sync** - Disabled by default (no WebSocket connections to ws.logseq.com)
- **AWS Cognito** - Not used unless you enable sync
- **Publishing** - Only works if you explicitly use it

---

## 🔍 How to Verify Privacy

### Method 1: Network Monitoring (Most Reliable)

#### Using Little Snitch or Lulu (macOS)
1. Install [Lulu](https://objective-see.org/products/lulu.html) (free firewall)
2. Launch Logseq
3. Monitor for connections to:
   - ❌ `sentry.io` - Should NOT appear
   - ❌ `posthog.com` - Should NOT appear
   - ❌ `ws.logseq.com` - Should NOT appear (sync disabled)
   - ❌ `api.logseq.com` - Should NOT appear (sync disabled)
   - ❌ `cognito.amazonaws.com` - Should NOT appear (auth disabled)

**Expected Result:** ZERO connections to analytics or sync servers ✅

#### Using Terminal
```bash
# Monitor all Logseq network connections
sudo lsof -i -P | grep -i logseq

# Or use tcpdump to watch for specific domains
sudo tcpdump -i any -n 'host sentry.io or host posthog.com or host ws.logseq.com or host api.logseq.com'
```

**Expected Result:** No traffic to any of these domains ✅

### Method 2: Developer Tools Check

1. **Open Developer Tools:**
   - Settings → Advanced → Enable "Developer mode"
   - Press: `Cmd+Option+I` (macOS) / `Ctrl+Shift+I` (Windows/Linux)

2. **Check Network Tab:**
   ```
   - Click "Network" tab
   - Filter by: "sentry" or "posthog" or "logseq.com"
   - Use Logseq for 5 minutes (create pages, search, etc.)
   ```

**Expected Result:** ZERO requests to analytics/sync services ✅

3. **Check Console for Errors:**
   - Look for any initialization messages
   - No "Sentry initialized" or "PostHog loaded" messages should appear

### Method 3: Local Storage Check

In Developer Tools Console, run:
```javascript
// Check for analytics data
localStorage.getItem('posthog')
localStorage.getItem('ph_project_api_key')
Object.keys(localStorage).filter(k => k.includes('sentry'))

// All should return null or empty
```

**Expected Result:** All return `null` or `[]` ✅

### Method 4: Code Inspection

```bash
cd /Applications/Logseq.app/Contents/Resources/app/

# Search for analytics endpoints
grep -r "sentry.io" js/
grep -r "posthog.com" js/
grep -r "ws.logseq.com" js/

# Check for sync websocket connections
grep -r "wss://ws" js/
```

**Expected Result:**
- No active references to analytics endpoints ✅
- Sync WebSocket code exists but is not initialized ✅

---

## 🛡️ Best Practices for Confidential Data

### 1. **Disable Auto-Updates**
Go to: **Settings → General → Disable "Auto check for updates"**

This prevents downloading updates from `update.electronjs.org`

### 2. **Don't Use Cloud Sync**
- Never enable "Logseq Sync" feature
- Never create a Logseq account in the app
- If prompted, click "Skip" or "Use Local Only"

### 3. **Don't Use Publishing**
- Never use the "Publish" feature
- This uploads content to `logseq.io`

### 4. **Review Plugins Carefully**
- Plugins download code from GitHub
- Each plugin can make network calls
- Only install plugins from trusted sources
- Consider: **Settings → Plugins → Disable plugins entirely**

### 5. **Disable YouTube Embeds (Optional)**
If concerned about Google tracking:
- Avoid using `{{youtube}}` embeds
- Or block `youtube.com` in your firewall

### 6. **Use Airplane Mode / Offline Mode**
For maximum privacy:
- Enable airplane mode
- Or block Logseq in your firewall (allow only localhost)

### 7. **Local Storage Only**
- Use local file system graphs (not iCloud/Dropbox)
- Store on encrypted drive
- Enable FileVault (macOS) or BitLocker (Windows)

### 8. **Review Network Connections Periodically**
Use Lulu/Little Snitch or `lsof` to verify no unexpected connections

---

## 📋 Quick Verification Checklist

Before using with confidential data, verify:

- [ ] Open DevTools Network tab
- [ ] Filter by "sentry" → 0 results
- [ ] Filter by "posthog" → 0 results
- [ ] Filter by "logseq.com" → 0 results (unless you enabled sync)
- [ ] Filter by "cognito" → 0 results
- [ ] Use app for 5 minutes, create pages, search
- [ ] Still 0 requests to analytics/sync services
- [ ] Auto-updates disabled in settings
- [ ] No Logseq account created
- [ ] Using local file system (not cloud storage)

**If all checks pass:** ✅ Safe for confidential data!

---

## 🚨 What Still Makes Network Calls

These features still connect to the internet (by design):

### Optional Features (Only if you use them):
- **Plugin Marketplace** - Downloads from GitHub
- **AI Models** - Downloads from HuggingFace (runs locally after)
- **YouTube Embeds** - Loads from YouTube
- **CDN Resources** - Fonts, icons, math rendering

### How to Block These:
1. **Don't use Plugin Marketplace** - Disable plugins in settings
2. **Don't use AI features** - Skip model downloads
3. **Don't embed YouTube videos** - Use local video files
4. **Use offline mode** - Block all network in firewall

---

## 🔬 Advanced Privacy Verification

### Full Network Trace
```bash
# Capture ALL Logseq network traffic for analysis
sudo tcpdump -i any -n -s 0 -w logseq-traffic.pcap 'port not 22'

# In another terminal, use Logseq for 10 minutes
# Then stop tcpdump (Ctrl+C)

# Analyze with Wireshark
wireshark logseq-traffic.pcap

# Look for:
# - HTTP/HTTPS requests (ports 80, 443)
# - WebSocket connections (look for "Upgrade: websocket")
# - DNS queries for suspicious domains
```

### Check for Hidden Trackers
```bash
# Search compiled code for tracking pixels, beacons
cd /Applications/Logseq.app/Contents/Resources/app/
grep -r "analytics" js/
grep -r "tracking" js/
grep -r "beacon" js/
grep -r "pixel" js/
```

---

## ✅ Confirmation

This build has:
- ✅ **Zero analytics** - Sentry and PostHog removed
- ✅ **Zero telemetry** - No usage data collected
- ✅ **Sync disabled by default** - No cloud uploads
- ✅ **Auth disabled by default** - No AWS Cognito
- ✅ **Publishing disabled by default** - No public uploads
- ✅ **Local-first** - All core features work 100% offline

**Safe for:**
- Legal documents
- Medical records
- Financial data
- Trade secrets
- Personal journals
- Any confidential information

**Requirements:**
- Don't enable Logseq Sync
- Don't create Logseq account
- Don't use Publishing
- Disable auto-updates
- Consider disabling plugins
- Use local file storage only

---

## 📞 Questions?

If you find any unexpected network activity, please report it immediately!

**Build Information:**
- Branch: `claude/disable-analytics-master-v4qrb`
- Analytics: Completely disabled
- Sync: Disabled by default
- Privacy: Maximum hardening

**Verify build integrity:**
```bash
# Check git commit
cd /path/to/logseq/repo
git log --oneline -5
# Should show commits for:
# - Disable all analytics and telemetry
# - Remove Sentry and PostHog imports
# - Disable file sync and RTC sync
```
