#!/usr/bin/env python3
"""
tools/curriculum_batch_001/deep_reading_expander.py

Injects 7-8 full paragraphs (1,050 to 1,200 words each) into 12 B2, C1, and C2 reading articles.
Then calibrates word_count and estimated_reading_minutes across all 50 reading articles.
"""

import os
import sys
from pathlib import Path
import yaml

project_root = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(project_root))

def get_deep_12_articles():
    # Load base expansions
    from tools.curriculum_batch_001.harden_reading_curriculum import EXPANSIONS
    from tools.curriculum_batch_001.reading_expansions_part2 import EXPANSIONS_PART2

    combined = dict(EXPANSIONS)
    combined.update(EXPANSIONS_PART2)

    # Now define supplementary paragraphs for each of the target articles to exceed 1,050 words
    ADDITIONS = {
        "reading.b2.continuous-integration-evolution": [
            {
                "paragraph_index": 6,
                "title": "Automated Security Governance and DevSecOps",
                "content_en": (
                    "In modern high-consequence enterprise environments, continuous integration pipelines have expanded "
                    "far beyond functional testing to encompass automated security governance, a paradigm known as DevSecOps. "
                    "Historically, security was treated as an isolated gatekeeping audit conducted manually by specialized teams "
                    "just days before a major release. This legacy approach created massive friction, often halting deployments "
                    "and forcing rushed architectural compromises. Modern CI pipelines embed static application security testing "
                    "(SAST) tools that scan source code for SQL injection vulnerabilities and hardcoded credentials on every commit. "
                    "Software composition analysis (SCA) scanners inspect third-party dependencies against national vulnerability "
                    "databases, instantly blocking builds that introduce critical CVEs. Dynamic application security testing (DAST) "
                    "runs automated penetration attacks against ephemeral staging instances. By shifting security leftward into "
                    "the automated developer loop, organizations achieve continuous compliance with stringent regulatory frameworks "
                    "like SOC2 and ISO 27001 without introducing bureaucratic approval delays or compromising developer velocity."
                ),
                "content_tr": (
                    "Modern yüksek riskli kurumsal ortamlarda, sürekli entegrasyon boru hatları fonksiyonel testlerin çok ötesine "
                    "geçerek DevSecOps olarak bilinen otomatik güvenlik yönetimini kapsayacak şekilde genişlemiştir. Tarihsel olarak "
                    "güvenlik, büyük bir sürümden birkaç gün önce uzman ekipler tarafından manuel olarak yürütülen izole bir denetim "
                    "olarak ele alınırdı. Bu eski yaklaşım, genellikle dağıtımları durdurarak ve aceleci mimari tavizleri zorlayarak "
                    "muazzam bir sürtüşme yaratırdı. Modern CI boru hatları, her commit'te kaynak kodunu SQL enjeksiyonu açıkları ve "
                    "sabit kodlanmış kimlik bilgileri açısından tarayan statik uygulama güvenliği testi (SAST) araçlarını gömer. Yazılım "
                    "bileşimi analizi (SCA) tarayıcıları, üçüncü taraf bağımlılıkları ulusal güvenlik açığı veritabanlarına karşı denetler "
                    "ve kritik CVE'ler getiren derlemeleri anında engeller. Dinamik uygulama güvenliği testi (DAST), geçici hazırlık "
                    "örneklerine karşı otomatik sızma saldırıları yürütür. Güvenliği otomatik geliştirici döngüsünün soluna kaydırarak "
                    "(shift-left), kurumlar bürokratik onay gecikmeleri getirmeden veya geliştirici hızından ödün vermeden SOC2 ve "
                    "ISO 27001 gibi katı yasal çerçevelere sürekli uyum sağlar."
                )
            },
            {
                "paragraph_index": 7,
                "title": "DORA Metrics and the Culture of Continuous Improvement",
                "content_en": (
                    "To objectively measure the efficacy of continuous integration, technology leaders turn to the four core "
                    "metrics established by the DevOps Research and Assessment (DORA) consortium: deployment frequency, lead time "
                    "for changes, mean time to restore service (MTTR), and change failure rate. Elite engineering organizations "
                    "utilize these empirical indicators to diagnose pipeline bottlenecks, whether caused by slow integration tests, "
                    "inefficient container build caches, or manual staging sign-offs. Continuous integration transforms engineering "
                    "from an unpredictable artisanal craft into a disciplined, data-driven manufacturing system. By providing "
                    "developers with rapid, high-fidelity feedback loops, CI fosters a blameless culture focused on systemic reliability. "
                    "Teams celebrate trunk stability, treat build failures as urgent collective emergencies, and continuously refine "
                    "their automation toolchains, ensuring that software delivery remains a sustainable engine of enterprise competitive advantage."
                ),
                "content_tr": (
                    "Sürekli entegrasyonun etkinliğini nesnel olarak ölçmek için teknoloji liderleri, DevOps Araştırma ve Değerlendirme "
                    "(DORA) konsorsiyumu tarafından belirlenen dört temel metriğe başvurur: dağıtım sıklığı, değişiklikler için sağlama süresi "
                    "(lead time), hizmeti geri yükleme ortalama süresi (MTTR) ve değişiklik başarısızlık oranı. Seçkin mühendislik "
                    "organizasyonları; ister yavaş entegrasyon testlerinden, ister verimsiz konteyner derleme önbelleklerinden, ister manuel "
                    "onaylardan kaynaklansın, boru hattı darboğazlarını teşhis etmek için bu ampirik göstergeleri kullanır. Sürekli "
                    "entegrasyon, mühendisliği öngörülemeyen zanaatkarca bir uğraştan disiplinli, veri odaklı bir üretim sistemine dönüştürür. "
                    "Geliştiricilere hızlı ve yüksek doğrulukta geri bildirim döngüleri sağlayarak CI, sistemsel güvenilirliğe odaklanan suçlayıcı "
                    "olmayan bir kültürü teşvik eder. Ekipler ana dal kararlılığını kutlar, derleme hatalarını acil kolektif durumlar olarak "
                    "ele alır ve otomasyon araç zincirlerini sürekli olarak geliştirerek yazılım teslimatının sürdürülebilir bir kurumsal "
                    "rekabet avantajı motoru olarak kalmasını sağlar."
                )
            }
        ],

        "reading.b2.leadership-emotional-intelligence": [
            {
                "paragraph_index": 6,
                "title": "Navigating Architectural Disagreements and Constructive Conflict",
                "content_en": (
                    "In sophisticated software engineering squads, architectural disagreements are not only inevitable but fundamentally "
                    "healthy for systemic resilience. When choosing between competing messaging brokers, caching tiers, or database engines, "
                    "passionate engineers naturally advocate for contrasting design patterns. Leaders lacking emotional intelligence often "
                    "perceive technical debates as personal insubordination, imposing unilateral executive decisions that crush team engagement. "
                    "Conversely, emotionally intelligent leaders reframe conflict from personal confrontation into collaborative inquiry. "
                    "They establish clear decision-making frameworks, such as Architecture Decision Records (ADRs) and time-boxed spike evaluations. "
                    "By facilitating rigorous, respectful technical debates where every dissenting viewpoint is analyzed objectively on its "
                    "architectural trade-offs, leaders build squad consensus, eliminate lingering resentment, and arrive at superior system designs."
                ),
                "content_tr": (
                    "Gelişmiş yazılım mühendisliği ekiplerinde mimari anlaşmazlıklar yalnızca kaçınılmaz olmakla kalmaz, sistemsel dayanıklılık "
                    "için temelde sağlıklıdır. Rakip mesaj aracıları, önbellek katmanları veya veritabanı motorları arasında seçim yaparken, tutkulu "
                    "mühendisler doğal olarak çelişen tasarım modellerini savunurlar. Duygusal zekadan yoksun liderler, teknik tartışmaları "
                    "genellikle kişisel itaatsizlik olarak algılar ve ekip katılımını ezen tek taraflı idari kararlar dayatırlar. Buna karşılık, "
                    "duygusal zekaya sahip liderler çatışmayı kişisel yüzleşmeden işbirlikçi bir araştırmaya dönüştürürler. Mimari Karar Kayıtları "
                    "(ADR'ler) ve zaman sınırlı araştırma (spike) değerlendirmeleri gibi net karar alma çerçeveleri oluştururlar. Her farklı "
                    "bakış açısının mimari ödünleşimleri üzerinde nesnel olarak analiz edildiği titiz, saygılı teknik tartışmaları kolaylaştırarak "
                    "liderler; ekip uzlaşısı oluşturur, kalıcı kırgınlıkları ortadan kaldırır ve üstün sistem tasarımlarına ulaşırlar."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Sustaining Team Energy and Eliminating Toxic Heroics",
                "content_en": (
                    "A chronic vulnerability in high-growth technology startups is the glorification of 'hero culture'—relying on a handful "
                    "of burnt-out senior engineers working eighty-hour weeks to compensate for systemic architectural debt and chaotic planning. "
                    "Emotionally mature engineering managers recognize that sustained heroic overwork is an operational antipattern that masks "
                    "deeper organizational dysfunction. They actively monitor team cognitive load, audit sprint velocity for signs of chronic fatigue, "
                    "and enforce healthy boundaries around on-call shifts. By establishing sustainable engineering rhythms, cross-training team members "
                    "to eliminate single points of failure, and treating employee well-being as a mission-critical operational KPI, emotionally "
                    "intelligent leaders build antifragile squads capable of consistent, high-velocity innovation across multi-year horizons."
                ),
                "content_tr": (
                    "Hızla büyüyen teknoloji girişimlerinde kronik bir kırılganlık, 'kahraman kültürünün' yüceltilmesidir; sistemsel mimari borçları "
                    "ve kaotik planlamayı telafi etmek için haftada seksen saat çalışan bir avuç tükenmiş kıdemli mühendise güvenmek. Duygusal "
                    "olarak olgun mühendislik yöneticileri, sürdürülen kahramanca aşırı çalışmanın daha derin örgütsel işlevsizliği gizleyen bir "
                    "operasyonel anti-örüntü olduğunu kabul ederler. Ekip bilişsel yükünü aktif olarak izler, sprint hızını kronik yorgunluk "
                    "belirtileri açısından denetler ve nöbet (on-call) vardiyaları etrafında sağlıklı sınırlar uygularlar. Sürdürülebilir "
                    "mühendislik ritimleri oluşturarak, tek hata noktalarını ortadan kaldırmak için ekip üyelerine çapraz eğitim vererek ve "
                    "çalışan refahını görev açısından kritik bir operasyonel KPI olarak ele alarak, duygusal zekaya sahip liderler çok yıllı ufuklarda "
                    "tutarlı, yüksek hızlı inovasyon yapabilen anti-kırılgan ekipler inşa ederler."
                )
            }
        ],

        "reading.b2.micro-frontend-paradigms": [
            {
                "paragraph_index": 6,
                "title": "Build Tooling, Dependency Governance, and Version Locking",
                "content_en": (
                    "One of the most insidious operational hazards in decentralized micro-frontend architectures is dependency drift. If left "
                    "unmonitored, independent engineering squads may inadvertently bundle incompatible versions of fundamental libraries, forcing "
                    "the client browser to download duplicate utility runtimes and crippling Core Web Vitals performance. To counter this decay, "
                    "forward-thinking frontend infrastructure teams enforce centralized governance via automated monorepo management tools like "
                    "Turborepo or Nx. These orchestrators enforce strict version-locking policies across shared package manifests, manage build "
                    "caching across remote container registries, and guarantee that shared core dependencies are deduplicated during dynamic module "
                    "federation handshakes. By pairing decentralized operational deployment with centralized dependency policy governance, technology "
                    "organizations harness the velocity of micro-frontends without sacrificing web performance or client runtime efficiency."
                ),
                "content_tr": (
                    "Merkeziyetsiz mikro ön uç mimarilerindeki en sinsi operasyonel tehlikelerden biri bağımlılık kaymasıdır (dependency drift). "
                    "İzlenmeden bırakılırsa, bağımsız mühendislik ekipleri istemeden temel kütüphanelerin uyumsuz sürümlerini paketleyebilir, istemci "
                    "tarayıcısını yinelenen yardımcı çalışma zamanlarını indirmeye zorlayabilir ve Core Web Vitals performansını felç edebilir. "
                    "Bu bozulmaya karşı koymak için ileri görüşlü ön uç altyapı ekipleri, Turborepo veya Nx gibi otomatik monorepo yönetim araçları "
                    "aracılığıyla merkezi yönetişim uygular. Bu yöneticiler; paylaşılan paket bildirimleri genelinde katı sürüm kilitleme politikaları "
                    "uygular, uzak konteyner kayıt defterleri genelinde derleme önbelleğe alımını yönetir ve dinamik modül federasyonu el sıkışmaları "
                    "sırasında paylaşılan çekirdek bağımlılıkların tekilleştirilmesini garanti eder. Merkeziyetsiz operasyonel dağıtımı merkezi "
                    "bağımlılık politikası yönetişimiyle eşleştirerek teknoloji organizasyonları, web performansından veya istemci çalışma zamanı "
                    "verimliliğinden ödün vermeden mikro ön uçların hızından yararlanır."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Comprehensive Testing Strategies for Composed Web Applications",
                "content_en": (
                    "Testing distributed client architectures requires a paradigm shift away from traditional end-to-end monolithic suites toward "
                    "rigorous contract testing. In a micro-frontend topology, individual squad testing focuses on isolated unit tests and visual "
                    "regression suites running against mock container shells. However, to guarantee seamless runtime composition, infrastructure "
                    "teams deploy automated contract verification using tools like Pact, ensuring that custom event schemas, URI route parameters, "
                    "and shared authentication tokens adhere to agreed specifications. Synthetic end-to-end user journey tests are executed against "
                    "canary environments to catch unexpected CSS cascade collisions or z-index stacking conflicts before releases hit production. "
                    "This multi-layered testing topology provides engineering squads with absolute confidence to deploy autonomous frontend artifacts "
                    "dozens of times daily without destabilizing the overarching digital user experience."
                ),
                "content_tr": (
                    "Dağıtık istemci mimarilerini test etmek, geleneksel uçtan uca monolitik paketlerden titiz sözleşme testine (contract testing) "
                    "doğru bir paradigma kayması gerektirir. Bir mikro ön uç topolojisinde, bireysel ekip testi sahte (mock) konteyner kabuklarına "
                    "karşı çalışan izole birim testlerine ve görsel regresyon paketlerine odaklanır. Ancak sorunsuz çalışma zamanı birleşimini "
                    "garanti etmek için altyapı ekipleri, Pact gibi araçları kullanarak otomatik sözleşme doğrulaması dağıtır; özel olay şemalarının, "
                    "URI rota parametrelerinin ve paylaşılan kimlik doğrulama belirteçlerinin üzerinde anlaşılan özelliklere uymasını sağlar. "
                    "Sürümler üretime geçmeden önce beklenmeyen CSS basamaklı çakışmalarını veya z-endeksi yığınlama çakışmalarını yakalamak için "
                    "kanarya ortamlarına karşı sentetik uçtan uca kullanıcı yolculuğu testleri yürütülür. Bu çok katmanlı test topolojisi, mühendislik "
                    "ekiplerine genel dijital kullanıcı deneyimini istikrarsızlaştırmadan otonom ön uç çıktılarını günde onlarca kez dağıtma konusunda mutlak bir güven sağlar."
                )
            }
        ],

        "reading.c1.zero-trust-security-paradigms": [
            {
                "paragraph_index": 6,
                "title": "Continuous Attestation and Behavioral Telemetry Analytics",
                "content_en": (
                    "In a mature Zero Trust ecosystem, authorization is not a discrete checkpoint passed at session login; it is an uninterrupted, "
                    "real-time evaluation known as continuous attestation. As an employee or workload executes tasks across enterprise cloud services, "
                    "a telemetry pipeline analyzes contextual signals in real time: geographic anomaly detection (such as simultaneous logins from "
                    "London and Tokyo indicating credential theft), device health metrics (such as the sudden deactivation of endpoint encryption or "
                    "jailbreak detection), and behavioral deviations from baseline usage patterns. If an engineer's workstation begins querying sensitive "
                    "financial databases outside typical working hours or staging massive bulk downloads from cloud storage buckets, dynamic risk-scoring "
                    "algorithms immediately downgrade the device's trust score. The identity proxy can trigger a step-up biometric challenge, revoke "
                    "active session tokens, or isolate the endpoint into a quarantined subnet, suffocating potential compromises in real time."
                ),
                "content_tr": (
                    "Olgun bir Sıfır Güven ekosisteminde yetkilendirme, oturum açma sırasında geçilen ayrı bir kontrol noktası değildir; sürekli "
                    "onaylama (continuous attestation) olarak bilinen kesintisiz, gerçek zamanlı bir değerlendirmedir. Bir çalışan veya iş yükü "
                    "kurumsal bulut servisleri genelinde görevleri yürütürken, bir telemetri boru hattı bağlamsal sinyalleri gerçek zamanlı olarak "
                    "analiz eder: coğrafi anomali tespiti (kimlik hırsızlığını gösteren Londra ve Tokyo'dan eşzamanlı oturum açmalar gibi), cihaz "
                    "sağlığı metrikleri (uç nokta şifrelemesinin aniden devre dışı bırakılması veya jailbreak tespiti gibi) ve temel kullanım "
                    "modellerinden davranışsal sapmalar. Bir mühendisin iş istasyonu tipik çalışma saatleri dışında hassas finansal veritabanlarını "
                    "sorgulamaya veya bulut depolama demetlerinden büyük toplu indirmeler yapmaya başlarsa, dinamik risk puanlama algoritmaları "
                    "cihazın güven puanını hemen düşürür. Kimlik vekili bir biyometrik doğrulama tetikleyebilir, aktif oturum belirteçlerini iptal "
                    "edebilir veya uç noktayı karantinaya alınmış bir alt ağa izole ederek olası ihlalleri gerçek zamanlı olarak boğabilir."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Sociotechnical Realities: Agility via Automated Cryptographic Federation",
                "content_en": (
                    "A common misconception among legacy infrastructure administrators is that Zero Trust imposes suffocating bureaucratic friction "
                    "that cripples developer agility. In truth, an engineered Zero Trust Architecture fundamentally liberates engineering squads from "
                    "brittle manual networking overhead. In legacy corporate networks, developers spent weeks submitting IT ticketing requests to open "
                    "firewall ports, modify static IP routing tables, and configure fragile point-to-point VPN tunnels. Under Zero Trust, cryptographic "
                    "identity federation replaces manual network plumbing with declarative code. Workloads dynamically establish ephemeral mutual TLS "
                    "connections using SPIFFE/SPIRE identity documents issued automatically in CI/CD pipelines. Remote engineers access internal "
                    "development environments seamlessly through identity-aware proxies without running battery-draining VPN clients. Zero Trust "
                    "proves that uncompromising cryptographic security and elite developer velocity are not opposing forces, but synergistic partners in modern digital resilience."
                ),
                "content_tr": (
                    "Eski altyapı yöneticileri arasındaki yaygın bir yanılgı, Sıfır Güven'in geliştirici hızını felç eden boğucu bürokratik sürtüşmeler "
                    "dayattığıdır. Gerçekte, iyi tasarlanmış bir Sıfır Güven Mimarisi, mühendislik ekiplerini kırılgan manuel ağ ek yükünden temelden "
                    "kurtarır. Eski kurumsal ağlarda geliştiriciler; güvenlik duvarı bağlantı noktalarını açmak, statik IP yönlendirme tablolarını "
                    "değiştirmek ve kırılgan noktadan noktaya VPN tünelleri yapılandırmak için BT bildirim talepleri göndererek haftalar harcardı. "
                    "Sıfır Güven altında kriptografik kimlik federasyonu, manuel ağ tesisatını bildirimsel (declarative) kodla değiştirir. İş yükleri, "
                    "CI/CD boru hatlarında otomatik olarak verilen SPIFFE/SPIRE kimlik belgelerini kullanarak geçici karşılıklı TLS bağlantılarını "
                    "dinamik olarak kurar. Uzaktaki mühendisler, pil tüketen VPN istemcilerini çalıştırmadan kimlik bilinçli vekiller aracılığıyla "
                    "dahili geliştirme ortamlarına sorunsuzca erişirler. Sıfır Güven, tavizsiz kriptografik güvenlik ile seçkin geliştirici hızının "
                    "birbirine zıt güçler olmadığını, aksine modern dijital dayanıklılıkta sinerjik ortaklar olduğunu kanıtlar."
                )
            }
        ],

        "reading.c1.executive-crisis-communication": [
            {
                "paragraph_index": 6,
                "title": "Protecting Engineering Morale and Establishing Clear Cadences",
                "content_en": (
                    "During a catastrophic production crisis, executive leadership must serve as an impenetrable operational shield between "
                    "technical incident responders and external panic. When major cloud outages or data leaks occur, corporate board members, "
                    "anxious enterprise account managers, and investigative journalists deluge technical leads with frantic inquiries. If senior "
                    "executives fail to establish strict communication boundaries, engineers spend eighty percent of their energy fielding panicked "
                    "Slack messages rather than diagnosing memory leaks or patching zero-day vulnerabilities. An emotionally mature crisis executive "
                    "establishes a predictable, disciplined update cadence—such as hourly public status dashboard updates—and strictly forbids external "
                    "stakeholders from distracting technical squads. By safeguarding the engineering blast radius, leadership enables rapid, focused "
                    "remediation while projecting calm competence to financial markets."
                ),
                "content_tr": (
                    "Felaket düzeyindeki bir üretim krizi sırasında üst yönetim, teknik müdahale ekipleri ile dış panik arasında aşılmaz bir "
                    "operasyonel kalkan görevi görmelidir. Büyük bulut kesintileri veya veri sızıntıları meydana geldiğinde; yönetim kurulu üyeleri, "
                    "endişeli kurumsal hesap yöneticileri ve araştırmacı gazeteciler teknik liderleri çılgınca sorularla boğarlar. Kıdemli yöneticiler "
                    "katı iletişim sınırları oluşturamazlarsa, mühendisler enerjilerinin yüzde seksenini bellek sızıntılarını teşhis etmek veya sıfır "
                    "gün açıklarını yamamak yerine panik dolu Slack mesajlarını yanıtlamakla harcarlar. Duygusal açıdan olgun bir kriz yöneticisi, "
                    "saatlik genel durum panosu güncellemeleri gibi öngörülebilir, disiplinli bir güncelleme ritmi belirler ve harici paydaşların "
                    "teknik ekiplerin dikkatini dağıtmasını kesinlikle yasaklar. Mühendislik patlama yarıçapını koruyarak liderlik, finansal "
                    "piyasalara sakin bir yetkinlik yansıtırken hızlı ve odaklanmış bir düzeltme sağlar."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Turning Catastrophe into Institutional Distinction",
                "content_en": (
                    "The defining hallmark of visionary corporate crisis leadership is the ability to transform catastrophic failure into enduring "
                    "institutional distinction. When cloud provider Cloudflare suffered a major edge routing outage caused by a regex catastrophic "
                    "backtracking bug, executive leadership did not issue evasive legal disclaimers. Within twelve hours, they published an exhaustive, "
                    "transparent post-mortem containing complete architectural diagrams, source code snippets, and minute-by-minute system metrics. "
                    "The global developer community lauded the company's radical candor, transforming a potentially fatal public relations disaster "
                    "into an industry-wide masterclass in systems engineering. Organizations that respond to crises with empirical humility, open-source "
                    "their remediation tools, and invest heavily in structural resilience emerge with unshakeable market trust and an unassailable reputation for integrity."
                ),
                "content_tr": (
                    "Vizyoner kurumsal kriz liderliğinin belirleyici ayırt edici özelliği, felaket düzeyindeki başarısızlığı kalıcı bir kurumsal "
                    "itibara dönüştürme yeteneğidir. Bulut sağlayıcısı Cloudflare, regex geri izleme hatasından kaynaklanan büyük bir uç yönlendirme "
                    "kesintisi yaşadığında, üst yönetim kaçamak yasal açıklamalar yapmadı. On iki saat içinde eksiksiz mimari diyagramlar, kaynak kod "
                    "parçacıkları ve dakika dakika sistem metriklerini içeren kapsamlı, şeffaf bir post-mortem yayınladılar. Küresel geliştirici topluluğu, "
                    "şirketin radikal dürüstlüğünü övdü ve potansiyel olarak ölümcül bir halkla ilişkiler felaketini sistem mühendisliğinde sektör çapında "
                    "bir ustalık sınıfına dönüştürdü. Krizlere ampirik tevazu ile yanıt veren, düzeltme araçlarını açık kaynaklı hale getiren ve yapısal "
                    "dayanıklılığa yoğun yatırım yapan kurumlar; sarsılmaz bir pazar güveni ve tartışılmaz bir dürüstlük itibarıyla ortaya çıkarlar."
                )
            }
        ],

        "reading.c1.behavioral-economics-product-choice": [
            {
                "paragraph_index": 6,
                "title": "Algorithmic Filtering and the Paradox of Digital Choice",
                "content_en": (
                    "In early digital commerce, product catalogs competed on sheer inventory volume, presuming that consumers maximized utility "
                    "when presented with infinite variety. However, cognitive psychologist Barry Schwartz's 'Paradox of Choice' demonstrated that "
                    "excessive variety paralyses decision-making, escalates buyer anxiety, and drastically increases post-purchase regret. When "
                    "confronted with hundreds of SaaS configurations or content items, cognitive bandwidth collapses, causing users to abandon onboarding "
                    "flows entirely. To remediate choice paralysis, modern digital platforms utilize personalized algorithmic recommendation engines. "
                    "By curating tailored subsets of options based on historical preference signals, systems reduce choice friction. Yet this convenience "
                    "introduces new cognitive hazards: over-filtering narrows intellectual serendipity and traps users in algorithmic echo chambers, "
                    "demanding that product architects carefully balance guided curation with exploratory autonomy."
                ),
                "content_tr": (
                    "Erken dijital ticarette ürün katalogları, tüketicilerin sonsuz çeşitlilik sunulduğunda faydayı maksimize ettiğini varsayarak salt "
                    "envanter hacmi üzerinden rekabet etti. Ancak bilişsel psikolog Barry Schwartz'ın 'Seçim Paradoksu' (Paradox of Choice), aşırı "
                    "çeşitliliğin karar almayı felç ettiğini, alıcı kaygısını tırmandırdığını ve satın alma sonrası pişmanlığı şiddetle artırdığını "
                    "gösterdi. Yüzlerce SaaS yapılandırması veya içerik öğesiyle karşılaşıldığında bilişsel bant genişliği çöker ve kullanıcıların "
                    "katılım akışlarını tamamen terk etmesine neden olur. Seçim felcini gidermek için modern dijital platformlar kişiselleştirilmiş "
                    "algoritmik öneri motorlarını kullanır. Sistemler, geçmiş tercih sinyallerine dayalı olarak özel seçenek alt kümeleri sunarak "
                    "seçim sürtüşmesini azaltır. Yine de bu kolaylık yeni bilişsel tehlikeler getirir: aşırı filtreleme entelektüel tesadüfleri (serendipity) "
                    "daraltır ve kullanıcıları algoritmik yankı odalarına hapseder; bu da ürün mimarlarının rehberli küratörlük ile keşif özerkliğini "
                    "dikkatlice dengelemesini gerektirir."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Ethical Nudging versus Deceptive Manipulation",
                "content_en": (
                    "The defining ethical boundary in behavioral product design hinges on intent: does a behavioral nudge genuinely advance the user's "
                    "self-professed welfare, or does it covertly extract commercial value through predatory friction? In their landmark work Nudge, "
                    "Richard Thaler and Cass Sunstein advocated for 'libertarian paternalism'—shaping choice environments to encourage beneficial "
                    "outcomes (such as saving money or completing privacy audits) while preserving complete freedom to opt out. Conversely, predatory "
                    "dark patterns subvert user autonomy by introducing asymmetric friction: making subscription enrollment a frictionless one-click "
                    "experience while burying cancellation behind labyrinths of multi-step phone confirmation calls. Ethical software companies build "
                    "enduring enterprise value by designing interfaces that honor user autonomy, establishing transparent pricing models, and treating "
                    "customer cognitive well-being as a sacred institutional responsibility."
                ),
                "content_tr": (
                    "Davranışsal ürün tasarımındaki belirleyici etik sınır niyete dayanır: davranışsal bir dürtme (nudge), kullanıcının kendi beyan "
                    "ettiği refahını gerçekten ilerletiyor mu, yoksa yırtıcı sürtüşmeler yoluyla gizlice ticari değer mi çekiyor? Çığır açan Nudge "
                    "eserlerinde Richard Thaler ve Cass Sunstein 'özgürlükçü paternalizmi' savundular; tamamen vazgeçme özgürlüğünü korurken faydalı "
                    "çıktıları (para biriktirmek veya gizlilik denetimlerini tamamlamak gibi) teşvik etmek için seçim ortamlarını şekillendirmek. Buna "
                    "karşılık yırtıcı karanlık desenler, asimetrik sürtüşmeler getirerek kullanıcı özerkliğini baltalar: abonelik kaydını sürtünmesiz "
                    "tek tıklamalı bir deneyim haline getirirken, iptali çok adımlı telefon onay görüşmelerinden oluşan labirentlerin arkasına gömer. "
                    "Etik yazılım şirketleri; kullanıcı özerkliğine saygı duyan arayüzler tasarlayarak, şeffaf fiyatlandırma modelleri oluşturarak ve "
                    "müşteri bilişsel refahını kutsal bir kurumsal sorumluluk olarak ele alarak kalıcı kurumsal değer inşa ederler."
                )
            }
        ],

        "reading.c1.distributed-consensus-systems": [
            {
                "paragraph_index": 6,
                "title": "Log Compaction, Snapshotting, and Practical Cluster Scaling",
                "content_en": (
                    "In theoretical computer science papers, consensus protocols are assumed to operate on unbounded, infinite append-only logs. "
                    "In production enterprise engineering, however, physical servers possess finite RAM and finite NVMe disk capacity. If a Raft or Paxos "
                    "cluster runs continuously for months, processing thousands of transactional state mutations every second, the append-only log expands "
                    "to terabytes, making node restarts painfully slow. To remain viable, consensus engines implement log compaction via periodic "
                    "snapshotting. A node atomically serializes its in-memory state machine to disk and discards all preceding log entries up to that commit index. "
                    "If a lagging or newly partitioned node rejoins the cluster, the leader avoids streaming millions of individual log entries; instead, "
                    "it transmits a single compacted snapshot over the wire, allowing the replica to rapidly catch up without exhausting cluster network bandwidth."
                ),
                "content_tr": (
                    "Teorik bilgisayar bilimi makalelerinde konsensüs protokollerinin sınırsız, sonsuz salt-eklemeli (append-only) günlükler üzerinde "
                    "çalıştığı varsayılır. Ancak üretim kurumsal mühendisliğinde fiziksel sunucular sonlu RAM ve sonlu NVMe disk kapasitesine sahiptir. "
                    "Bir Raft veya Paxos kümesi aylarca sürekli çalışır ve her saniye binlerce işlemsel durum mutasyonunu işlerse, salt-eklemeli günlük "
                    "terabaytlara ulaşarak düğüm yeniden başlatmalarını acı verici derecede yavaşlatır. Yaşayabilir kalmak için konsensüs motorları "
                    "periyodik anlık görüntü alma (snapshotting) yoluyla günlük sıkıştırması uygular. Bir düğüm, bellek içi durum makinesini atomik "
                    "olarak diske serileştirir ve o onay indeksine kadar olan önceki tüm günlük girişlerini atar. Geciken veya yeni bölümlenmiş bir düğüm "
                    "kümeye yeniden katılırsa, lider milyonlarca bireysel günlük girişini akıtmaktan kaçınır; bunun yerine hat üzerinden tek bir "
                    "sıkıştırılmış anlık görüntü ileterek replikanın küme ağ bant genişliğini tüketmeden hızla yetişmesini sağlar."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Planetary Scale and the TrueTime Revolution",
                "content_en": (
                    "The frontiers of distributed consensus advanced dramatically with the introduction of Google Spanner, the world's first globally "
                    "distributed database providing linearizable externally consistent transactions at planetary scale. Historically, distributed systems "
                    "could not rely on physical hardware wall clocks because clock drift across servers creates non-deterministic orderings. Spanner "
                    "overcame this physical barrier by engineering TrueTime: an API backed by synchronized GPS atomic clocks and cesium oscillators "
                    "deployed in every data center. TrueTime explicitly exposes clock uncertainty as a bounded interval [earliest, latest]. By forcing "
                    "transactions to wait out the maximum clock uncertainty before committing, Spanner guarantees absolute chronological serialization "
                    "across continents. TrueTime proved that combining rigorous consensus algorithms with hardware-assisted time synchronization allows "
                    "distributed systems to transcend the traditional latency limitations of classical asynchronous consensus."
                ),
                "content_tr": (
                    "Dağıtık konsensüsün sınırları, gezegen ölçeğinde doğrusallaştırılabilir dışsal olarak tutarlı işlemler sağlayan dünyanın ilk küresel "
                    "dağıtık veritabanı olan Google Spanner'ın tanıtımıyla çarpıcı bir şekilde ilerledi. Tarihsel olarak dağıtık sistemler, sunucular "
                    "arasındaki saat kayması deterministik olmayan sıralamalar yarattığından fiziksel donanım duvar saatlerine güvenemiyordu. Spanner, "
                    "TrueTime'ı tasarlayarak bu fiziksel engeli aştı: her veri merkezinde konuşlandırılmış senkronize GPS atomik saatleri ve sezyum osilatörleri "
                    "tarafından desteklenen bir API. TrueTime, saat belirsizliğini açıkça sınırlı bir aralık [en erken, en geç] olarak gösterir. İşlemleri "
                    "onaylamadan önce maksimum saat belirsizliği süresi boyunca beklemeye zorlayarak Spanner, kıtalar arasında mutlak kronolojik "
                    "serileştirmeyi garanti eder. TrueTime; titiz konsensüs algoritmalarını donanım destekli zaman senkronizasyonuyla birleştirmenin, "
                    "dağıtık sistemlerin klasik asenkron konsensüsün geleneksel gecikme sınırlamalarını aşmasına olanak tanıdığını kanıtladı."
                )
            }
        ],

        "reading.c2.epistemic-foundations-of-science": [
            {
                "paragraph_index": 6,
                "title": "Bayesian Epistemology and Probabilistic Credence Updating",
                "content_en": (
                    "To transcend the rigid binary absolutes of naive Popperian falsificationism—where a single contrary anomaly theoretically annihilates "
                    "an entire theoretical framework—contemporary philosophers of science widely embrace Bayesian epistemology. Rather than viewing "
                    "hypotheses as categorically proven or disproven, Bayesianism conceptualizes belief as a continuous spectrum of subjective probabilities "
                    "known as credences. Using Bayes' Theorem, a scientist updates their prior probability in a hypothesis upon observing new empirical "
                    "evidence, multiplied by the likelihood ratio of the evidence given the theory versus alternative paradigms. Bayesianism elegantly explains "
                    "why the historical discovery of anomalous planetary orbits did not immediately demolish Newtonian gravitational physics: the prior "
                    "credence in Newtonian mechanics was exceptionally high, logically justifying the hypothesis that an unobserved planet (subsequently discovered "
                    "as Neptune) was perturbing the orbit, rather than abandoning fundamental physics."
                ),
                "content_tr": (
                    "Tek bir karşıt anomalinin teorik olarak tüm bir teorik çerçeveyi yok ettiği naif Popperian yanlışlamacılığın katı ikili mutlaklarını "
                    "aşmak için, çağdaş bilim felsefecileri yaygın olarak Bayesçi epistemolojiyi benimserler. Hipotezleri kategorik olarak kanıtlanmış veya "
                    "çürütülmüş olarak görmek yerine Bayesçilik, inancı 'itimatlar' (credences) olarak bilinen öznel olasılıkların sürekli bir spektrumu "
                    "olarak kavramsallaştırır. Bayes Teoremini kullanan bir bilim insanı, yeni ampirik kanıtları gözlemlediğinde bir hipotezdeki önceki "
                    "olasılığını (prior), alternatif paradigmalara karşı teoriye dayalı kanıtın olabilirlik oranıyla çarparak günceller. Bayesçilik, "
                    "anormal gezegen yörüngelerinin tarihsel keşfinin Newtoncu yerçekimi fiziğini neden hemen yıkmadığını zarif bir şekilde açıklar: "
                    "Newton mekaniğine olan önceki itimat son derece yüksekti ve temel fiziği terk etmek yerine gözlemlenmemiş bir gezegenin (daha sonra "
                    "Neptün olarak keşfedildi) yörüngeyi bozduğu hipotezini mantıksal olarak haklı çıkarıyordu."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Algorithmic Induction and the Future of the Scientific Method",
                "content_en": (
                    "In the contemporary era of deep neural networks and automated scientific discovery, the epistemological debate has acquired profound "
                    "computational urgency. Modern AI models ingest petabytes of experimental particle collision data, genomic sequences, and astronomical "
                    "spectroscopy, deriving predictive correlations that vastly exceed human analytical capacity. However, these deep learning systems "
                    "operate through pure statistical induction on continuous non-linear manifolds, lacking explicit mechanistic causal models. When a neural "
                    "network predicts protein folding with unprecedented accuracy without being able to articulate the underlying thermodynamic physical laws, "
                    "does it constitute genuine scientific understanding, or merely a hyper-sophisticated black-box instrument? As empirical discovery "
                    "becomes increasingly automated, natural philosophy must synthesize Popperian falsificationist rigor, Bayesian probabilistic updating, "
                    "and computational epistemology to ensure that scientific knowledge remains grounded in transparent, explanatory human reason."
                ),
                "content_tr": (
                    "Derin sinir ağları ve otomatik bilimsel keşiflerin çağdaş döneminde, epistemolojik tartışma derin bir hesaplamalı aciliyet kazanmıştır. "
                    "Modern yapay zeka modelleri petabaytlarca deneysel parçacık çarpışma verisini, genomik dizilimleri ve astronomik spektroskopiyi "
                    "özümseyerek insan analitik kapasitesini fersah fersah aşan öngörüsel korelasyonlar türetir. Ancak bu derin öğrenme sistemleri, açık "
                    "mekanistik nedensel modellerden yoksun olarak, sürekli doğrusal olmayan manifoldlar üzerinde saf istatistiksel tümevarım yoluyla "
                    "çalışır. Bir sinir ağı, altta yatan termodinamik fizik yasalarını ifade edemeden benzeri görülmemiş bir doğrulukla protein katlanmasını "
                    "tahmin ettiğinde, bu gerçek bir bilimsel anlayış mı oluşturur, yoksa yalnızca aşırı gelişmiş bir kara kutu aracı mı? Ampirik keşif "
                    "giderek daha fazla otomatik hale geldikçe, doğa felsefesi; bilimsel bilginin şeffaf, açıklayıcı insani akla dayalı kalmasını sağlamak "
                    "için Popperian yanlışlamacı titizliği, Bayesçi olasılıksal güncellemeyi ve hesaplamalı epistemolojiyi sentezlemelidir."
                )
            }
        ],

        "reading.c2.the-mechanics-of-speculative-bubbles": [
            {
                "paragraph_index": 6,
                "title": "Macroprudential Governance and the Central Banking Dilemma",
                "content_en": (
                    "The persistent recurrence of speculative manias poses a profound operational dilemma for modern central bankers and financial regulators. "
                    "During the inflation of a bubble, political pressure against regulatory intervention is intense. Venture capitalists, real estate tycoons, "
                    "and elected politicians fiercely denounce any attempts to tighten monetary policy, claiming that regulators are suffocating economic "
                    "innovation and technological disruption. Former Federal Reserve Chairman Alan Greenspan famously championed the 'doctrine of benign neglect,' "
                    "arguing that central banks cannot identify speculative bubbles in real time and should merely wait to clean up the wreckage through interest "
                    "rate cuts after the bubble bursts. However, the catastrophic aftermath of the 2008 crash proved that retrospective cleanup is a ruinous "
                    "strategy. Modern regulatory orthodoxy advocates for countercyclical macroprudential tools: dynamically raising bank capital reserve "
                    "buffers and enforcing strict loan-to-value limits during economic booms to deflate asset bubbles before they imperil systemic solvency."
                ),
                "content_tr": (
                    "Spekülatif çılgınlıkların kalıcı olarak tekrarlanması, modern merkez bankacıları ve finansal düzenleyiciler için derin bir operasyonel "
                    "ikilem oluşturur. Bir balonun şişmesi sırasında, düzenleyici müdahaleye karşı siyasi baskı son derece yoğundur. Girişim sermayedarları, "
                    "gayrimenkul kodamanları ve seçilmiş politikacılar; düzenleyicilerin ekonomik inovasyonu ve teknolojik atılımı boğduğunu iddia ederek "
                    "para politikasını sıkılaştırmaya yönelik her türlü girişimi şiddetle kınarlar. Eski Federal Rezerv Başkanı Alan Greenspan, merkez "
                    "bankalarının spekülatif balonları gerçek zamanlı olarak tanımlayamayacağını ve balon patladıktan sonra faiz indirimleri yoluyla "
                    "enkazı temizlemeyi beklemeleri gerektiğini savunarak ünlü 'iyi huylu ihmal doktrinini' (doctrine of benign neglect) savundu. "
                    "Ancak 2008 krizinin felaket niteliğindeki sonuçları, geriye dönük temizliğin yıkıcı bir strateji olduğunu kanıtladı. Modern düzenleyici "
                    "ortodoksluk, döngü karşıtı makroihtiyati araçları savunmaktadır: varlık balonlarını sistemik ödeme gücünü tehlikeye atmadan önce "
                    "söndürmek için ekonomik patlamalar sırasında banka sermaye rezerv tamponlarını dinamik olarak artırmak ve katı kredi-değer limitleri uygulamak."
                )
            },
            {
                "paragraph_index": 7,
                "title": "The Moral Hazard of Systemic Bailouts and Future Vulnerability",
                "content_en": (
                    "When speculative manias inevitably crash, governments confront the terrifying prospect of total financial meltdown. To avert systemic "
                    "depression, sovereign states execute massive multi-trillion-dollar liquidity bailouts, guaranteeing interbank deposits and purchasing "
                    "impaired corporate debt. While these emergency interventions stabilize immediate panic, they introduce catastrophic moral hazard into "
                    "the financial architecture. By privatizing astronomical profits during the speculative bubble while socializing catastrophic losses "
                    "during the bust, governments implicitly signal to financial institutions that high-risk speculative behavior carries state-backed insurance. "
                    "Financial titans are incentivized to become 'too big to fail,' maximizing systemic leverage in the certainty that public treasuries "
                    "will rescue them when the bubble pops. Breaking this destructive cycle requires structural market separation, unyielding executive clawbacks, "
                    "and the unshakeable institutional discipline to allow reckless speculative capital to absorb its own losses."
                ),
                "content_tr": (
                    "Spekülatif çılgınlıklar kaçınılmaz olarak çöktüğünde, hükümetler tam bir finansal erime gibi korkunç bir olasılıkla karşı karşıya kalırlar. "
                    "Sistemik bir bunalımı önlemek için egemen devletler, bankalararası mevduatları garanti ederek ve batık kurumsal borçları satın alarak "
                    "devasa çok trilyon dolarlık likidite kurtarma operasyonları yürütürler. Bu acil durum müdahaleleri anlık paniği yatıştırsa da, finansal "
                    "mimariye felaket boyutunda bir ahlaki tehlike (moral hazard) sokarlar. Spekülatif balon sırasında astronomik karları özelleştirirken, "
                    "çöküş sırasında felaket boyutundaki kayıpları sosyalleştirerek hükümetler; finansal kurumlara yüksek riskli spekülatif davranışların "
                    "devlet destekli sigorta taşıdığını örtük olarak bildirir. Finans devleri, balon patladığında kamu hazinelerinin onları kurtaracağı "
                    "kesinliğiyle sistemik kaldıracı maksimize ederek 'batamayacak kadar büyük' (too big to fail) olmaya teşvik edilir. Bu yıkıcı döngüyü kırmak; "
                    "yapısal pazar ayrımını, tavizsiz yönetici tazminat geri alımlarını (clawbacks) ve pervasız spekülatif sermayenin kendi kayıplarını "
                    "üstlenmesine izin verecek sarsılmaz kurumsal disiplini gerektirir."
                )
            }
        ],

        "reading.c2.architectural-modularity-and-technical-debt": [
            {
                "paragraph_index": 6,
                "title": "Automated Architectural Fitness Functions in CI/CD",
                "content_en": (
                    "In large-scale enterprise codebases undergoing rapid feature development by hundreds of distributed engineers, architectural "
                    "principles cannot rely on good intentions or manual documentation alone. Over time, well-meaning developers inadvertently "
                    "introduce forbidden dependencies—such as importing a database repository directly into a domain entity or bypassing presentation "
                    "layer interfaces. To enforce boundary hygiene continuously, progressive engineering organizations institutionalize 'architectural "
                    "fitness functions' within their continuous integration pipelines. Using automated code analysis frameworks like ArchUnit, engineers "
                    "write executable unit tests that assert structural invariants: verifying that domain layers contain zero dependencies on external "
                    "libraries, ensuring circular module dependencies trigger immediate build failures, and guaranteeing package encapsulation rules. "
                    "By embedding architectural governance directly into automated build gates, organizations protect modular integrity programmatically."
                ),
                "content_tr": (
                    "Yüzlerce dağıtık mühendis tarafından hızlı özellik geliştirme sürecinden geçen büyük ölçekli kurumsal kod tabanlarında mimari ilkeler, "
                    "yalnızca iyi niyetlere veya manuel belgelere dayanamaz. Zamanla, iyi niyetli geliştiriciler farkında olmadan yasaklanmış bağımlılıklar "
                    "getirirler; örneğin bir veritabanı deposunu doğrudan bir alan varlığına aktarmak veya sunum katmanı arayüzlerini atlamak gibi. Sınır "
                    "hijyenini sürekli olarak uygulamak için ilerici mühendislik organizasyonları, sürekli entegrasyon boru hatları içinde 'mimari uygunluk "
                    "fonksiyonlarını' (architectural fitness functions) kurumsallaştırır. ArchUnit gibi otomatik kod analiz çatılarını kullanan mühendisler, "
                    "yapısal değişmezleri doğrulayan çalıştırılabilir birim testler yazarlar: alan katmanlarının harici kütüphanelere sıfır bağımlılık "
                    "içerdiğini doğrulamak, dairesel modül bağımlılıklarının derleme hatalarını anında tetiklemesini sağlamak ve paket kapsülleme kurallarını "
                    "garanti etmek. Mimari yönetişimi doğrudan otomatik derleme kapılarına gömerek kurumlar, modüler bütünlüğü programatik olarak korurlar."
                )
            },
            {
                "paragraph_index": 7,
                "title": "Technical Debt Economics and Continuous Refactoring Culture",
                "content_en": (
                    "Ultimately, managing architectural modularity is an economic discipline governed by disciplined capital allocation. In modern "
                    "corporate finance, neglecting maintenance on physical manufacturing assets leads to asset impairment write-downs; similarly, "
                    "neglecting technical debt in software assets leads to systemic engineering bankruptcy. Elite technology enterprises combat this "
                    "entropy by institutionalizing non-negotiable engineering capacity—typically twenty percent of every sprint cycle—dedicated exclusively "
                    "to structural refactoring, dependency upgrades, and technical debt retirement. Rather than attempting massive, high-risk multi-year "
                    "system rewrites that frequently end in catastrophic project abandonment, mature engineering teams practice the 'Boy Scout Rule': "
                    "leaving the codebase cleaner than they found it with every commit. By respecting modularity as a living, evolving ecosystem, "
                    "technology companies preserve agility and ensure software longevity across generational technological shifts."
                ),
                "content_tr": (
                    "Nihayetinde mimari modülerliği yönetmek, disiplinli sermaye tahsisi tarafından yönetilen ekonomik bir disiplindir. Modern kurumsal "
                    "finansta, fiziksel üretim varlıklarının bakımını ihmal etmek varlık değer düşüklüğü zararlarına yol açar; benzer şekilde, yazılım "
                    "varlıklarındaki teknik borcu ihmal etmek sistemik mühendislik iflasına yol açar. Seçkin teknoloji işletmeleri, her sprint döngüsünün "
                    "pazarlık konusu edilemez bir mühendislik kapasitesini —tipik olarak yüzde yirmisini— yalnızca yapısal yeniden yapılandırmaya, "
                    "bağımlılık yükseltmelerine ve teknik borç tasfiyesine ayırarak bu entropiyle mücadele eder. Çoğunlukla felaket boyutunda proje "
                    "terkleriyle sonuçlanan devasa, yüksek riskli çok yıllı sistem yeniden yazımlarına kalkışmak yerine olgun mühendislik ekipleri "
                    "'İzci Kuralı'nı (Boy Scout Rule) uygular: her commit ile kod tabanını bulduklarından daha temiz bırakmak. Modülerliğe yaşayan, "
                    "gelişen bir ekosistem olarak saygı göstererek teknoloji şirketleri çevikliği korur ve nesiller boyu teknolojik değişimler boyunca yazılımın uzun ömürlülüğünü sağlar."
                )
            }
        ],

        "reading.c2.algorithmic-governance-and-ethics": [
            {
                "paragraph_index": 6,
                "title": "Mechanistic Interpretability and the Right to Explanation",
                "content_en": (
                    "To overcome the profound legal and constitutional dilemma of opaque AI black boxes, artificial intelligence researchers and legal "
                    "scholars are pioneering the discipline of mechanistic interpretability. Rather than relying on superficial post-hoc feature importance "
                    "metrics like SHAP or LIME—which can easily be fooled by adversarial perturbations—mechanistic interpretability seeks to reverse-engineer "
                    "the internal computational circuits and neural representations of deep transformer models. In administrative and criminal law, "
                    "jurisdictions are codifying a non-negotiable 'Right to Explanation.' Under this emergent jurisprudence, any automated system that "
                    "deprives an individual of liberty, financial credit, employment, or housing must provide a legible, contestable rationale that "
                    "articulates the counterfactual causal conditions: 'What specific attributes of the application would have yielded an affirmative "
                    "decision?' Without mechanistic interpretability, algorithmic adjudication reduces citizens to passive subjects of an arbitrary digital bureaucracy."
                ),
                "content_tr": (
                    "Opak yapay zeka kara kutularının derin yasal ve anayasal ikileminin üstesinden gelmek için yapay zeka araştırmacıları ve hukuk "
                    "akademisyenleri mekanistik yorumlanabilirlik (mechanistic interpretability) disiplinine öncülük ediyorlar. Çekişmeli pertürbasyonlar "
                    "tarafından kolayca kandırılabilen SHAP veya LIME gibi yüzeysel post-hoc özellik önem metriklerine güvenmek yerine, mekanistik "
                    "yorumlanabilirlik, derin dönüştürücü modellerin dahili hesaplama devrelerini ve sinirsel temsillerini tersine mühendislikle "
                    "çözmeyi amaçlar. İdare ve ceza hukukunda yargı bölgeleri, pazarlık konusu edilemez bir 'Açıklama Hakkı'nı kanunlaştırmaktadır. "
                    "Bu gelişen içtihat altında, bir bireyi özgürlüğünden, finansal kredisinden, istihdamından veya barınmasından mahrum bırakan herhangi "
                    "bir otomatik sistem; karşı-olgusal nedensel koşulları açıkça ifade eden okunabilir, itiraz edilebilir bir gerekçe sunmalıdır: "
                    "'Başvurunun hangi belirli nitelikleri olumlu bir karar verdirirdi?' Mekanistik yorumlanabilirlik olmadan algoritmik yargılama, "
                    "vatandaşları keyfi bir dijital bürokrasinin pasif tebaasına indirger."
                )
            },
            {
                "paragraph_index": 7,
                "title": "A Jurisprudence of Algorithmic Proportionality",
                "content_en": (
                    "Preserving constitutional democracy in an automated civilization requires formulating a robust 'jurisprudence of algorithmic "
                    "proportionality.' This legal doctrine asserts that the acceptable level of algorithmic autonomy must be strictly inversely proportional "
                    "to the consequence of the decision. While low-consequence automated workflows—such as spam filtering, video compression bitrate "
                    "adaptation, or automated route optimization—can operate with high statistical autonomy, high-consequence adjudications impacting human "
                    "rights must remain anchored to strict human oversight. Machines must never be granted unreviewable authority to execute lethal military "
                    "force, impose criminal prison sentences, or deny political asylum. By enforcing rigorous algorithmic red-teaming, funding independent "
                    "public audit institutions, and enshrining human dignity as an immutable constitutional invariant, democratic societies ensure that "
                    "technological progress serves human liberation rather than algorithmic subjugation."
                ),
                "content_tr": (
                    "Otomatikleştirilmiş bir medeniyette anayasal demokrasiyi korumak, sağlam bir 'algoritmik orantılılık içtihadı' formüle etmeyi "
                    "gerektirir. Bu hukuk doktrini, kabul edilebilir algoritmik özerklik düzeyinin kararın etkisiyle kesinlikle ters orantılı olması "
                    "gerektiğini ileri sürer. İstenmeyen e-posta filtreleme, video sıkıştırma bit hızı uyarlaması veya otomatik rota optimizasyonu gibi "
                    "düşük etkili otomatik iş akışları yüksek istatistiksel özerklikle çalışabilirken; insan haklarını etkileyen yüksek riskli yargılamalar "
                    "katı insan gözetimine bağlı kalmalıdır. Makinelere asla ölümcül askeri güç kullanma, adli hapis cezası verme veya siyasi sığınma "
                    "hakkını reddetme konusunda incelenemez bir yetki verilmemelidir. Titiz algoritmik kırmızı takım (red-teaming) testleri uygulayarak, "
                    "bağımsız kamu denetim kurumlarını finanse ederek ve insan onurunu değişmez bir anayasal ilke olarak koruyarak demokratik toplumlar; "
                    "teknolojik ilerlemenin algoritmik boyun eğdirme yerine insanın özgürleşmesine hizmet etmesini sağlar."
                )
            }
        ]
    }

    # Merge supplementary paragraphs into combined
    for k, add_paras in ADDITIONS.items():
        if k in combined:
            combined[k].extend(add_paras)

    return combined

def run_deep_expansion():
    reading_dir = project_root / "content" / "reading"
    yaml_files = sorted(reading_dir.rglob("*.yaml"))

    expanded_articles = get_deep_12_articles()
    total_articles = 0
    gt_1000 = 0
    results = []

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
            total_articles += 1

            if a_id in expanded_articles:
                paras = expanded_articles[a_id]
                # Re-index paragraphs 1..N cleanly
                for idx, p in enumerate(paras, 1):
                    p["paragraph_index"] = idx
                a["paragraphs"] = paras
                modified = True

            # Calibrate word count to match actual text
            actual_words = sum(len(p.get("content_en", "").split()) for p in a.get("paragraphs", []))
            a["word_count"] = actual_words
            a["estimated_reading_minutes"] = max(1, int(round(actual_words / 180)))
            modified = True

            if actual_words > 1000:
                gt_1000 += 1
            results.append((a_id, a.get("cefr_level"), actual_words))

        if modified:
            with open(ypath, "w", encoding="utf-8") as f:
                yaml.dump(articles, f, allow_unicode=True, sort_keys=False, width=120)
            print(f"[UPDATED] {ypath.name}")

    print("\n==================================================")
    print("Reading Corpus Hardening Summary")
    print("==================================================")
    print(f"Total Reading Articles Processed: {total_articles}")
    print(f"Articles genuinely > 1,000 words: {gt_1000} (Requirement: >= 10 in B2-C2)")
    print(f"Average article word count: {sum(w for _, _, w in results) / len(results):.1f} words")
    print("\nArticles > 1,000 words list:")
    for a_id, cefr, words in sorted(results, key=lambda x: -x[2]):
        if words > 1000:
            print(f"  - {a_id} ({cefr}): {words} words")

if __name__ == "__main__":
    run_deep_expansion()
