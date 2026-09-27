#!/usr/bin/env python3
"""
tools/curriculum_batch_001/top_up_readings.py

Tops up the 8 target articles so that all 13 target articles genuinely exceed 1,000 words.
Synchronizes word_count and estimated_reading_minutes across all 50 reading articles.
"""

from pathlib import Path
import yaml

project_root = Path(__file__).resolve().parent.parent.parent

# Expansions for the 8 articles
EXTENSIONS = {
    "reading.b2.leadership-emotional-intelligence": (
        " Beyond individual interactions, high emotional intelligence transforms how technical squads respond to operational emergencies. "
        "When catastrophic database failovers or distributed cache corruptions occur, engineers do not waste valuable time shielding themselves "
        "from punitive blame. Instead, fostered by a climate of transparent vulnerability and collective accountability, squads immediately "
        "swarm the issue, share live diagnostic telemetry, and execute collaborative rollbacks. Leadership that prioritizes psychological "
        "health and constructive empathy creates an enduring cultural foundation where technical brilliance can flourish without burning out."
    ),
    "reading.b2.leadership-emotional-intelligence_tr": (
        " Bireysel etkileşimlerin ötesinde, yüksek duygusal zeka teknik ekiplerin operasyonel acil durumlara nasıl yanıt verdiğini dönüştürür. "
        "Felaket düzeyinde veritabanı yük devretmeleri veya dağıtık önbellek bozulmaları meydana geldiğinde, mühendisler kendilerini cezalandırıcı "
        "suçlamalardan korumak için değerli zamanlarını harcamazlar. Bunun yerine, şeffaf kırılganlık ve kolektif hesap verebilirlik iklimiyle "
        "beslenen ekipler, soruna hemen birlikte müdahale eder, canlı teşhis telemetrisini paylaşır ve işbirlikçi geri almalar yürütürler. Psikolojik "
        "sağlığa ve yapıcı empatiye öncelik veren liderlik, teknik parlaklığın tükenmeden serpilebileceği kalıcı bir kültürel temel yaratır."
    ),

    "reading.b2.micro-frontend-paradigms": (
        " Furthermore, progressive organizations recognize that micro-frontend architectures demand continuous architectural observability. "
        "By integrating distributed client telemetry and automated Core Web Vitals performance budgets into daily reporting dashboards, "
        "engineering leaders instantly identify memory leaks, layout shifts, or bundle bloat introduced by rogue third-party packages. "
        "When paired with disciplined design token governance and automated contract tests, micro-frontends empower enterprise organizations "
        "to scale software delivery horizontally while preserving the polished, cohesive elegance of a unified digital flagship."
    ),
    "reading.b2.micro-frontend-paradigms_tr": (
        " Dahası, ilerici kurumlar mikro ön uç mimarilerinin sürekli mimari gözlemlenebilirlik talep ettiğini kabul eder. Dağıtık istemci "
        "telemetrisini ve otomatik Core Web Vitals performans bütçelerini günlük raporlama panolarına entegre ederek mühendislik liderleri; "
        "uyumsuz üçüncü taraf paketlerin neden olduğu bellek sızıntılarını, düzen kaymalarını veya paket şişkinliğini anında tespit ederler. "
        "Disiplinli tasarım belirteci yönetişimi ve otomatik sözleşme testleriyle eşleştirildiğinde mikro ön uçlar, kurumsal organizasyonların "
        "birleşik bir dijital amiral gemisinin zarif ve uyumlu zarafetini korurken yazılım teslimatını yatay olarak ölçeklendirmesini sağlar."
    ),

    "reading.c1.executive-crisis-communication": (
        " In the modern interconnected economy, executive crisis leadership is an ongoing test of organizational humility and ethical stamina. "
        "When public disclosures are delivered with complete factual clarity, leadership signals to enterprise clients, regulators, and shareholders "
        "that the company prioritizes systemic truth over superficial stock valuation. Organizations that consistently navigate adversity "
        "with transparency not only survive reputational tempests, but establish an unshakeable standard of executive excellence that inspires "
        "deep customer loyalty and enduring brand prestige across global markets."
    ),
    "reading.c1.executive-crisis-communication_tr": (
        " Modern bağlantılı ekonomide üst düzey kriz liderliği, örgütsel tevazu ve etik dayanıklılığın süregiden bir sınavıdır. Kamuya yapılan "
        "açıklamalar tam bir olgusal netlikle sunulduğunda liderlik kurumsal müşterilere, düzenleyicilere ve hissedarlara şirketin sistemsel "
        "gerçeği yüzeysel hisse senedi değerlemesinin üstünde tuttuğunu bildirir. Zorlukları sürekli olarak şeffaflıkla aşan kurumlar, yalnızca "
        "itibar fırtınalarından sağ çıkmakla kalmaz; küresel pazarlarda derin müşteri sadakati ve kalıcı marka prestijine ilham veren sarsılmaz bir "
        "yönetim mükemmelliği standardı oluştururlar."
    ),

    "reading.c1.behavioral-economics-product-choice": (
        " Ultimately, the commercial sustainability of digital product platforms depends on respecting consumer intentionality. "
        "When software companies design interfaces that eliminate cognitive friction while preserving user sovereignty, they create "
        "compounding organic customer retention. Behavioral economics proves that sustainable enterprise value is generated not through coercive "
        "neurological tricks, but through benevolent choice environments that empower users to achieve their genuine personal and professional goals."
    ),
    "reading.c1.behavioral-economics-product-choice_tr": (
        " Nihayetinde dijital ürün platformlarının ticari sürdürülebilirliği, tüketici niyetine saygı duymaya bağlıdır. Yazılım şirketleri "
        "kullanıcı egemenliğini korurken bilişsel sürtüşmeyi ortadan kaldıran arayüzler tasarladığında, katlanarak artan organik müşteri elde "
        "tutma oranı yaratırlar. Davranışsal ekonomi, sürdürülebilir kurumsal değerin zorlayıcı nörolojik hilelerle değil, kullanıcıları gerçek "
        "kişisel ve profesyonel hedeflerine ulaşmaları için güçlendiren yardımsever seçim ortamları aracılığıyla üretildiğini kanıtlar."
    ),

    "reading.c2.algorithmic-governance-and-ethics": (
        " Preserving the rule of law within automated societies demands continuous vigilance from legal scholars, software engineers, and civic leaders. "
        "When decision-making algorithms operate behind opaque commercial barricades without contestability, democratic sovereignty is subverted. "
        "By enforcing strict architectural interpretability, public bias audits, and non-negotiable human accountability for high-consequence outcomes, "
        "civilization ensures that technological progress elevates human flourishing rather than entrenching unaccountable technocratic dominion."
    ),
    "reading.c2.algorithmic-governance-and-ethics_tr": (
        " Otomatikleştirilmiş toplumlarda hukukun üstünlüğünü korumak, hukuk akademisyenlerinden, yazılım mühendislerinden ve sivil liderlerden "
        "sürekli uyanıklık talep eder. Karar alma algoritmaları itiraz edilebilirlik olmaksızın opak ticari barikatların arkasında çalıştığında, "
        "demokratik egemenlik baltalanır. Sıkı mimari yorumlanabilirlik, kamuya açık önyargı denetimleri ve yüksek riskli çıktılar için pazarlık "
        "konusu edilemez insani hesap verebilirlik uygulayarak medeniyet, teknolojik ilerlemenin hesap verilemez teknokratik egemenliği "
        "kemikleştirmek yerine insanın gelişip serpilmesini yükseltmesini sağlar."
    ),

    "reading.c2.architectural-modularity-and-technical-debt": (
        " In conclusion, treating architectural modularity as a primary asset rather than a secondary cosmetic concern is the ultimate "
        "differentiator of enduring technology firms. Codebases that enforce high cohesion, loose coupling, and hexagonal domain purity adapt "
        "effortlessly to shifting market demands, new cloud platforms, and evolving business paradigms. By investing continuously in technical debt "
        "remediation, elite software organizations build resilient systems that compound commercial value across decades of continuous innovation."
    ),
    "reading.c2.architectural-modularity-and-technical-debt_tr": (
        " Sonuç olarak, mimari modülerliği ikincil bir kozmetik endişe yerine birincil bir varlık olarak ele almak, kalıcı teknoloji firmalarının "
        "nihai ayırt edici özelliğidir. Yüksek bağıntı, gevşek bağımlılık ve altıgen alan saflığını uygulayan kod tabanları; değişen pazar taleplerine, "
        "yeni bulut platformlarına ve gelişen iş paradigmalarına zahmetsizce uyum sağlar. Teknik borç düzeltmeye sürekli yatırım yaparak seçkin "
        "yazılım organizasyonları, onlarca yıllık sürekli inovasyon boyunca ticari değer üreten dayanıklı sistemler inşa ederler."
    )
}

# 3 full paragraphs for cross-cultural negotiation to reach > 1000 words
CROSS_CULTURAL_ADDITIONS = [
    {
        "paragraph_index": 6,
        "title": "Deciphering Indirect Feedback and Non-Verbal Nuance",
        "content_en": (
            "In high-context commercial cultures, direct disagreement or blunt rejection is regarded as socially abrasive and destructive to interpersonal "
            "harmony. Where an American executive expects a crisp 'no' during contract negotiations, Japanese, Turkish, or Middle Eastern counterparts "
            "may respond with polite ambiguity, prolonged silence, or gentle phrases like 'that could be difficult.' Inexperienced Western negotiators "
            "frequently misinterpret these polite hesitation markers as conditional agreement, pushing ahead aggressively and inadvertently causing severe "
            "loss of face. Skilled cross-cultural negotiators learn to read the rich contextual cues: posture, vocal inflection, unstated hesitation, and "
            "post-meeting side conversations. By honoring diplomatic reticence and allowing partners to preserve face, leaders uncover underlying objections, "
            "re-calibrate terms with sensitivity, and build profound mutual trust that withstands market volatility."
        ),
        "content_tr": (
            "Yüksek bağlamlı ticari kültürlerde, doğrudan anlaşmazlık veya açık ret, sosyal olarak yıpratıcı ve kişilerarası uyumu yıkıcı olarak kabul "
            "edilir. Bir Amerikalı yöneticinin sözleşme müzakereleri sırasında net bir 'hayır' beklediği durumlarda, Japon, Türk veya Orta Doğulu muadiller "
            "kibar bir belirsizlik, uzun süreli sessizlik veya 'bu biraz zor olabilir' gibi nazik ifadelerle yanıt verebilirler. Deneyimsiz Batılı "
            "müzakereciler, bu kibar tereddüt işaretlerini sıklıkla şartlı bir anlaşma olarak yanlış yorumlar; agresif bir şekilde ısrar ederek istemeden "
            "ciddi bir itibar kaybına (loss of face) neden olurlar. Yetenekli kültürlerarası müzakereciler zengin bağlamsal ipuçlarını okumayı öğrenirler: "
            "duruş, ses tonu, dile getirilmemiş tereddüt ve toplantı sonrası yan konuşmalar. Diplomatik ketumluğa saygı göstererek ve ortakların itibarlarını "
            "korumalarına izin vererek liderler; altta yatan itirazları ortaya çıkarır, şartları duyarlılıkla yeniden ayarlar ve piyasa dalgalanmalarına "
            "dayanan derin bir karşılıklı güven inşa ederler."
        )
    },
    {
        "paragraph_index": 7,
        "title": "Consensus-Driven Decision Making versus Top-Down Executive Mandates",
        "content_en": (
            "Another critical fault line in international negotiation is the structural divergence in organizational decision-making models. Western corporate "
            "cultures champion individual executive agency, empowering a chief executive or procurement lead to make binding, unilateral commitments on the "
            "spot. In contrast, East Asian corporate governance often adheres to the Ringi system—a painstaking, bottom-up consensus mechanism where proposals "
            "circulate across multiple horizontal squads and vertical managerial layers before securing unanimous formal seal approval. Impatient Western "
            "executives who interpret this deliberate latency as administrative incompetence or disinterest often destroy nascent deals. Successful global "
            "dealmakers respect local governance workflows: they provide exhaustive documentation, support internal corporate champions, and maintain patient, "
            "consistent communication while consensus matures across the partner's organization."
        ),
        "content_tr": (
            "Uluslararası müzakerelerdeki bir diğer kritik fay hattı, örgütsel karar alma modellerindeki yapısal farklılıktır. Batı kurumsal kültürleri, "
            "bir genel müdüre veya satın alma liderine anında bağlayıcı, tek taraflı taahhütlerde bulunma yetkisi vererek bireysel yönetici iradesini savunur. "
            "Buna karşılık Doğu Asya kurumsal yönetişimi genellikle Ringi sistemine bağlı kalır: tekliflerin oybirliğiyle resmi onay almadan önce birden "
            "fazla yatay ekip ve dikey yönetim katmanı arasında dolaştığı özenli, aşağıdan yukarıya bir uzlaşı mekanizması. Bu bilinçli gecikmeyi idari "
            "yetersizlik veya ilgisizlik olarak yorumlayan sabırsız Batılı yöneticiler genellikle yeni başlayan anlaşmaları yok ederler. Başarılı küresel "
            "anlaşma yapıcılar yerel yönetişim iş akışlarına saygı gösterirler: kapsamlı belgeler sağlar, dahili kurumsal savunucuları destekler ve "
            "ortağın organizasyonunda uzlaşı olgunlaşırken sabırlı, tutarlı bir iletişim sürdürürler."
        )
    },
    {
        "paragraph_index": 8,
        "title": "The Enduring Power of Cross-Border Relational Capital",
        "content_en": (
            "In conclusion, cross-cultural negotiation is not an adversarial game of tactical coercion, but a collaborative art of bridging divergent "
            "worldviews. In the twenty-first-century global technology economy, cross-border mergers, strategic hardware partnerships, and international "
            "cloud migrations span continents and cultures. Executives who approach international dialogue with intellectual curiosity, emotional empathy, "
            "and unwavering respect for communicative traditions forge enduring commercial partnerships that transcend legal boilerplate. By mastering "
            "the delicate balance between contractual precision and cultural sensitivity, international leaders build resilient global enterprises capable "
            "of unlocking extraordinary multilateral value across interconnected global markets."
        ),
        "content_tr": (
            "Sonuç olarak kültürlerarası müzakere, taktiksel baskının uygulandığı düşmanca bir oyun değil; farklı dünya görüşleri arasında köprü kurma "
            "yönündeki işbirlikçi bir sanattır. Yirmi birinci yüzyılın küresel teknoloji ekonomisinde sınır ötesi birleşmeler, stratejik donanım "
            "ortaklıkları ve uluslararası bulut geçişleri kıtalara ve kültürlere yayılmaktadır. Uluslararası diyaloğa entelektüel merak, duygusal empati "
            "ve iletişimsel geleneklere sarsılmaz bir saygıyla yaklaşan yöneticiler; yasal basmakalıpları aşan kalıcı ticari ortaklıklar kurarlar. Sözleşme "
            "kesinliği ile kültürel duyarlılık arasındaki hassas dengeyi kurarak uluslararası liderler; birbirine bağlı küresel pazarlar genelinde "
            "olağanüstü çok taraflı değerin kilidini açabilen dayanıklı küresel işletmeler inşa ederler."
        )
    }
]

# 3 full paragraphs for monetary policy to reach > 1000 words
MONETARY_ADDITIONS = [
    {
        "paragraph_index": 6,
        "title": "Central Bank Digital Currencies and the Architecture of Modern Payments",
        "content_en": (
            "In response to the rapid rise of private cryptographic assets and the geopolitical weaponization of dollar payment clearing networks, "
            "central banks globally are architecting sovereign Central Bank Digital Currencies (CBDCs). Unlike commercial bank digital money—which "
            "represents liabilities of private lending institutions—a retail or wholesale CBDC constitutes a direct digital claim on the sovereign central "
            "bank balance sheet. Proponents emphasize that CBDCs dramatically reduce payment settlement friction, eliminate exorbitant cross-border remittance "
            "fees, and provide an unhackable sovereign backstop against private stablecoin systemic runs. However, CBDCs introduce formidable constitutional "
            "and systemic risks: programmed digital money could empower authoritarian regimes to execute real-time financial surveillance or restrict "
            "citizen spending, while disintermediating commercial banks by prompting wholesale deposit flight into the central bank during financial panics."
        ),
        "content_tr": (
            "Özel kripto varlıkların hızlı yükselişine ve dolar ödeme takas ağlarının jeopolitik silah haline getirilmesine yanıt olarak, dünya çapındaki "
            "merkez bankaları egemen Merkez Bankası Dijital Para Birimleri (CBDC'ler) tasarlamaktadır. Özel kredi kuruluşlarının yükümlülüklerini temsil "
            "eden ticari banka dijital parasının aksine, perakende veya toptan bir CBDC, doğrudan egemen merkez bankası bilançosu üzerinde dijital bir "
            "hak teşkil eder. Taraftarlar, CBDC'lerin ödeme takas sürtüşmesini önemli ölçüde azalttığını, fahiş sınır ötesi havale ücretlerini ortadan "
            "kaldırdığını ve özel sabit paralardaki (stablecoins) sistemik kaçışlara karşı sarsılmaz bir egemen güvenlik ağı sağladığını vurgulamaktadır. "
            "Bununla birlikte CBDC'ler müthiş anayasal ve sistemik riskler getirir: programlanmış dijital para, otoriter rejimlerin gerçek zamanlı finansal "
            "gözetim yürütmesine veya vatandaş harcamalarını kısıtlamasına olanak tanıyabilirken; finansal panikler sırasında merkez bankasına kitlesel "
            "mevduat kaçışını tetikleyerek ticari bankaları aracısızlaştırabilir."
        )
    },
    {
        "paragraph_index": 7,
        "title": "The Geopolitics of De-Dollarization and Multilateral Clearing",
        "content_en": (
            "The contemporary global monetary architecture is experiencing an unprecedented structural fracturing. Following the freezing of three hundred "
            "billion dollars in sovereign Russian central bank foreign exchange reserves by Western nations, sovereign states across the Global South "
            "accelerated strategic de-dollarization initiatives. The geopolitical weaponization of SWIFT and the international dollar reserve currency "
            "system demonstrated that holding offshore fiat reserves carries severe sovereign expropriation risk. Consequently, BRICS economies and bilateral "
            "trading blocs are pioneering local-currency settlement mechanisms, expanding gold reserves, and establishing alternative cross-border clearing "
            "protocols. While the dollar's deep, liquid capital markets guarantee its preeminence for the immediate future, the emergence of a multipolar "
            "currency architecture permanently diminishes Western monetary hegemony and fundamentally alters global trade finance."
        ),
        "content_tr": (
            "Çağdaş küresel parasal mimari, benzeri görülmemiş bir yapısal kırılma yaşamaktadır. Batılı uluslar tarafından üç yüz milyar dolarlık egemen "
            "Rus merkez bankası döviz rezervinin dondurulmasının ardından, Küresel Güney'deki egemen devletler stratejik dolarsızlaşma girişimlerini "
            "hızlandırdı. SWIFT'in ve uluslararası dolar rezerv para birimi sisteminin jeopolitik bir silaha dönüştürülmesi, denizaşırı itibari (fiat) "
            "rezerv tutmanın ciddi egemen kamulaştırma riski taşıdığını kanıtladı. Sonuç olarak, BRICS ekonomileri ve ikili ticaret blokları yerel para birimi "
            "takas mekanizmalarına öncülük etmekte, altın rezervlerini genişletmekte ve alternatif sınır ötesi takas protokolleri oluşturmaktadır. Doların "
            "derin, likit sermaye piyasaları yakın gelecek için üstünlüğünü garanti etse de, çok kutuplu bir para birimi mimarisinin ortaya çıkışı "
            "Batı parasal hegemonyasını kalıcı olarak azaltmakta ve küresel ticaret finansmanını temelden değiştirmektedir."
        )
    },
    {
        "paragraph_index": 8,
        "title": "Navigating the Macroeconomic Frontier: Stability versus Transformation",
        "content_en": (
            "In conclusion, modern macroeconomic policy has transcended the simplistic era of single-instrument interest rate modulation. Confronted "
            "with secular demographic aging, massive public debt burdens, geopolitical trade fragmentation, and climate transition financing needs, "
            "central banks and sovereign treasuries must synthesize unconventional monetary policies with disciplined fiscal governance. The challenge "
            "of the coming decades will be to foster sustainable real economic productivity while avoiding the dual traps of hyper-inflationary debt "
            "monetization and speculative financial collapse. Policymakers who navigate this precarious frontier with institutional humility, intellectual "
            "rigor, and structural foresight will safeguard sovereign economic vitality and lay the foundation for enduring global prosperity."
        ),
        "content_tr": (
            "Sonuç olarak modern makroekonomik politika, tek araçlı faiz oranı ayarlamasının basit dönemini aşmıştır. Seküler demografik yaşlanma, devasa "
            "kamu borç yükleri, jeopolitik ticaret parçalanması ve iklim geçişi finansmanı ihtiyaçlarıyla karşı karşıya kalan merkez bankaları ve egemen "
            "hazineler; geleneksel olmayan para politikalarını disiplinli mali yönetişimle sentezlemelidir. Gelecek on yılların meydan okuması, hiper "
            "enflasyonist borç monetizasyonu ve spekülatif finansal çöküş şeklindeki ikili tuzaktan kaçınırken sürdürülebilir reel ekonomik üretkenliği "
            "teşvik etmek olacaktır. Bu istikrarsız sınırda kurumsal tevazu, entelektüel titizlik ve yapısal öngörü ile yol alan politika yapıcılar; "
            "egemen ekonomik canlılığı güvence altına alacak ve kalıcı küresel refahın temelini atacaklardır."
        )
    }
]

def run():
    reading_dir = project_root / "content" / "reading"
    yaml_files = sorted(reading_dir.rglob("*.yaml"))

    for ypath in yaml_files:
        if "batches" in ypath.parts or "samples" in ypath.parts:
            continue

        with open(ypath, "r", encoding="utf-8") as f:
            articles = yaml.safe_load(f)

        if not isinstance(articles, list):
            continue

        modified = False
        for a in articles:
            a_id = a.get("id", "")

            # 1. Check if we need to append text to the final paragraph
            if a_id in EXTENSIONS:
                paras = a.get("paragraphs", [])
                if paras:
                    paras[-1]["content_en"] += EXTENSIONS[a_id]
                    tr_key = a_id + "_tr"
                    if tr_key in EXTENSIONS:
                        paras[-1]["content_tr"] += EXTENSIONS[tr_key]
                modified = True

            # 2. Check if we need to append multi-paragraphs
            if a_id == "reading.b2.cross-cultural-negotiation":
                paras = a.get("paragraphs", [])
                for p in CROSS_CULTURAL_ADDITIONS:
                    p["paragraph_index"] = len(paras) + 1
                    paras.append(p)
                a["paragraphs"] = paras
                modified = True

            if a_id == "reading.c2.monetary-policy-and-macro-imbalances":
                paras = a.get("paragraphs", [])
                for p in MONETARY_ADDITIONS:
                    p["paragraph_index"] = len(paras) + 1
                    paras.append(p)
                a["paragraphs"] = paras
                modified = True

            # Recalculate word_count and estimated_reading_minutes
            actual_words = sum(len(p.get("content_en", "").split()) for p in a.get("paragraphs", []))
            a["word_count"] = actual_words
            a["estimated_reading_minutes"] = max(1, int(round(actual_words / 180)))
            modified = True

        if modified:
            with open(ypath, "w", encoding="utf-8") as f:
                yaml.dump(articles, f, allow_unicode=True, sort_keys=False, width=120)
            print(f"[TOPPED UP] {ypath.name}")

if __name__ == "__main__":
    run()
