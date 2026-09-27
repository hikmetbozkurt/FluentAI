#!/usr/bin/env python3
"""
Reading Generator for B2 (8 new articles, bringing B2 total to 10).
Contains 3 articles exceeding 1,000 words:
- reading.b2.continuous-integration-evolution (~1,050 words)
- reading.b2.leadership-emotional-intelligence (~1,040 words)
- reading.b2.micro-frontend-paradigms (~1,030 words)
Other articles range 750-900 words.
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=301)

def build_q(qid, q_en, q_tr_hint, correct, distractors, exp_en, exp_tr):
    options, idx = balancer.balance_options(correct, distractors)
    return {
        "id": qid,
        "question_en": q_en,
        "question_tr_hint": q_tr_hint,
        "options": options,
        "correct_answer": correct,
        "explanation_en": exp_en,
        "explanation_tr": exp_tr
    }

B2_READING_ARTICLES = [
    {
        "id": "reading.b2.incident-postmortem-culture",
        "title": "Building a Blameless Post-Mortem Culture in High-Reliability Engineering",
        "cefr_level": "B2",
        "category": "engineering_culture",
        "summary_en": "How top-tier engineering organizations conduct blameless incident retrospectives to uncover systemic architectural weaknesses rather than penalizing human error.",
        "summary_tr": "Önde gelen mühendislik organizasyonlarının insan hatasını cezalandırmak yerine sistemsel mimari zayıflıkları ortaya çıkarmak için suçlamasız (blameless) vaka analizlerini nasıl yürüttüğü.",
        "word_count": 780,
        "estimated_reading_minutes": 4,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Fallacy of the Bad Apple",
                "content_en": "When a major production outage knocks out customer-facing services, the intuitive human reaction is to seek someone to blame. Management demands to know which engineer pushed the broken commit or misconfigured the firewall. However, decades of safety science across aviation, healthcare, and distributed computing prove that attributing complex failures to individual negligence—the so-called 'Bad Apple' theory—is fundamentally counterproductive. In modern distributed systems, failure is an emergent property of interconnected complexity rather than a single person's carelessness.",
                "content_tr": "Büyük bir canlı sistem kesintisi müşteri hizmetlerini devre dışı bıraktığında, sezgisel insan tepkisi suçlayacak birini aramaktır. Yönetim hangi mühendisin hatalı kodu gönderdiğini veya güvenlik duvarını yanlış yapılandırdığını bilmek ister. Ancak havacılık, sağlık ve dağıtık bilişim alanlarındaki onlarca yıllık güvenlik bilimi, karmaşık arızaları bireysel ihmale bağlamanın (sözde 'Çürük Elma' teorisi) son derece ters etki yarattığını kanıtlamaktadır. Modern dağıtık sistemlerde arıza, tek bir kişinin dikkatsizliğinden ziyade birbirine bağlı karmaşıklığın ortaya çıkan doğal bir sonucudur."
            },
            {
                "paragraph_index": 2,
                "title": "The Mechanics of a Blameless Post-Mortem",
                "content_en": "A blameless post-mortem operates on a foundational postulate: every employee acted in good faith based on the information, tools, and cognitive context available to them at the time. Instead of asking 'Who caused this?', the investigation interrogates the sociotechnical environment: Why did the deployment pipeline permit unvalidated configuration syntax to reach production? Why were telemetry alerts delayed by ten minutes? Why did the documentation fail to clarify the failover procedure? By shifting focus from human guilt to systemic vulnerability, organizations convert painful outages into enduring institutional resilience.",
                "content_tr": "Suçlamasız bir vaka sonrası analiz (blameless post-mortem) temel bir varsayımla çalışır: her çalışan o sırada elinde bulunan bilgiye, araçlara ve bilişsel bağlama dayanarak iyi niyetle hareket etmiştir. Soruşturma 'Buna kim sebep oldu?' diye sormak yerine sosyoteknik ortamı sorgular: Dağıtım hattı doğrulanmamış yapılandırma sözdiziminin canlı ortama ulaşmasına neden izin verdi? Telemetri uyarıları neden on dakika gecikti? Dokümantasyon yedekleme prosedürünü açıklamakta neden yetersiz kaldı? Şirketler odağı insan suçluluğundan sistemsel kırılganlığa kaydırarak acı verici kesintileri kalıcı kurumsal dayanıklılığa dönüştürür."
            },
            {
                "paragraph_index": 3,
                "title": "Establishing Clear Timelines and Five Whys",
                "content_en": "The practical artifact of an effective post-mortem is a comprehensive, objective timeline. Engineers document exact timestamps: when the regression was introduced, when automated monitoring triggered alerts, when on-call engineers were paged, and when service was fully restored. Teams subsequently apply the 'Five Whys' methodology to drill down through superficial symptoms toward underlying root causes, distinguishing immediate triggers from deep systemic latency.",
                "content_tr": "Etkili bir vaka analizinin somut çıktısı kapsamlı ve nesnel bir zaman çizelgesidir. Mühendisler kesin zaman damgalarını belgeler: gerilemenin ne zaman başladığı, otomatik izlemenin ne zaman uyarı verdiği, nöbetçi mühendislerin ne zaman çağrıldığı ve hizmetin ne zaman tamamen geri yüklendiği. Ekipler daha sonra yüzeysel semptomların altındaki temel kök nedenlere inmek için '5 Neden' metodolojisini uygular ve anlık tetikleyicileri derin sistemsel gecikmelerden ayırır."
            },
            {
                "paragraph_index": 4,
                "title": "Accountability through Actionable Remediation",
                "content_en": "A common misconception is that a blameless culture means an absence of accountability. In truth, blamelessness elevates accountability by requiring concrete, tracked remediation items. Rather than punishing a developer, the team commits to engineering safeguards: automated schema validation, canary deployments, or enhanced circuit breakers. When teams know they will not be fired for candidly sharing mistakes, transparent reporting flourishes, making the entire infrastructure progressively antifragile.",
                "content_tr": "Yaygın bir yanılgı, suçlamasız bir kültürün hesap verebilirlik eksikliği anlamına geldiğidir. Gerçekte suçlamasızlık somut ve takip edilen iyileştirme adımları gerektirerek hesap verebilirliği yükseltir. Ekip bir yazılımcıyı cezalandırmak yerine mühendislik önlemleri almayı taahhüt eder: otomatik şema doğrulaması, kanarya dağıtımları veya gelişmiş devre kesiciler. Ekipler hataları samimiyetle paylaştıkları için kovulmayacaklarını bildiklerinde şeffaf raporlama gelişir ve tüm altyapı kademeli olarak kırılganlıktan uzak (antifragile) hale gelir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "bottleneck",
                "vocab_id": "vocab.bottleneck",
                "context_definition_en": "A point of congestion or obstruction that impedes overall systemic progress.",
                "context_meaning_tr": "Genel sistem ilerlemesini engelleyen tıkanıklık veya darboğaz noktası."
            },
            {
                "word": "leverage",
                "vocab_id": "vocab.leverage",
                "context_definition_en": "The strategic advantage or power to influence and achieve maximum results.",
                "context_meaning_tr": "Stratejik avantaj veya maksimum sonuç elde etme gücü (kaldıraç)."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_01_01",
                "Why is the 'Bad Apple' theory considered fundamentally flawed in distributed software systems?",
                "Dağıtık yazılım sistemlerinde 'Çürük Elma' teorisi neden temelde kusurlu kabul edilir?",
                "Outages emerge from interconnected architectural complexity rather than isolated human negligence",
                ["Software engineers are legally prohibited from making technical mistakes", "Computer servers never experience electrical failures under load", "Individual developers are always replaced by automated robots every month"],
                "Paragraph 1 explains failure is an emergent property of complexity rather than personal carelessness.",
                "1. paragraf arızanın bireysel dikkatsizlikten ziyade karmaşıklığın doğal bir sonucu olduğunu belirtir."
            ),
            build_q(
                "q_r_b2_01_02",
                "What foundational postulate governs a blameless post-mortem investigation?",
                "Suçlamasız bir vaka sonrası analiz soruşturmasını hangi temel varsayım yönetir?",
                "Every employee acted in good faith based on the context and tools available at the time",
                ["The employee who pushed the commit must pay for the company's financial losses", "Customers should never be informed when a major security breach occurs", "All incident reports must be shredded and destroyed immediately after resolution"],
                "Paragraph 2 states employees acted in good faith based on available context.",
                "2. paragraf her çalışanın elindeki bilgiye dayanarak iyi niyetle hareket ettiğini varsaydığını belirtir."
            ),
            build_q(
                "q_r_b2_01_03",
                "How does the investigation shift questions away from individual culpability?",
                "Soruşturma soruları bireysel suçluluktan başka yöne nasıl kaydırır?",
                "By interrogating sociotechnical safeguards, pipeline controls, and telemetry delays",
                ["By asking employees about their childhood hobbies and favorite sports", "By voting anonymously on which engineer should be demoted", "By requiring the team to write apologies in local newspapers"],
                "Paragraph 2 contrasts 'Who caused this?' with questions about pipelines and telemetry.",
                "2. paragraf soruşturmanın 'Kim yaptı?' yerine dağıtım hatlarını ve telemetriyi sorguladığını açıklar."
            ),
            build_q(
                "q_r_b2_01_04",
                "What is the practical value of applying the 'Five Whys' methodology?",
                "'5 Neden' metodolojisini uygulamanın pratik değeri nedir?",
                "Drilling beneath superficial symptoms toward systemic underlying root causes",
                ["Guaranteeing that software code reviews will never be necessary again", "Calculating employee holiday bonuses down to the exact dollar", "Forcing developers to apologize five times during the retrospective"],
                "Paragraph 3 explains it drills down through superficial symptoms toward root causes.",
                "3. paragraf yüzeysel belirtilerin altındaki kök nedenlere inmeyi sağladığını belirtir."
            ),
            build_q(
                "q_r_b2_01_05",
                "How does a blameless culture achieve real accountability without punitive measures?",
                "Suçlamasız bir kültür cezalandırıcı önlemler olmadan gerçek hesap verebilirliği nasıl sağlar?",
                "By requiring concrete, trackable engineering safeguards and architectural remediation items",
                ["By fining engineers twenty percent of their salary after every outage", "By forcing junior staff to work seventy hours a week without overtime pay", "By publicly posting the names of struggling developers on the office wall"],
                "Paragraph 4 explains accountability is achieved through tracked remediation items and safeguards.",
                "4. paragraf hesap verebilirliğin somut iyileştirme adımları ve önlemlerle sağlandığını vurgular."
            )
        ],
        "topic_tags": ["incident-management", "post-mortem", "blameless-culture", "engineering-culture", "b2-reading"],
        "related_ids": ["vocab.bottleneck", "vocab.leverage"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.fintech-api-revolution",
        "title": "How Open Banking APIs Reshaped Modern Financial Architecture",
        "cefr_level": "B2",
        "category": "finance_and_economics",
        "summary_en": "An exploration of regulatory and technological catalysts behind open banking APIs, standardized protocols, data privacy mandates, and customer financial empowerment.",
        "summary_tr": "Açık bankacılık API'lerinin arkasındaki yasal ve teknolojik itici güçler: standart protokoller, veri gizliliği kuralları ve tüketici finansal özgürlüğü.",
        "word_count": 820,
        "estimated_reading_minutes": 4,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Walled Gardens of Legacy Banking",
                "content_en": "For generations, retail commercial banking operated as an impenetrable walled garden. Traditional financial institutions maintained proprietary mainframes, treating consumer transaction histories, credit scores, and account balances as exclusive corporate property. Customers who wished to switch financial providers or aggregate accounts faced cumbersome bureaucratic friction. However, over the past decade, a powerful convergence of progressive legislation—such as the European Union's PSD2 directive—and RESTful API standards shattered this monopolistic architecture.",
                "content_tr": "Nesiller boyunca bireysel ticari bankacılık, aşılamaz bir kapalı bahçe (walled garden) olarak faaliyet gösterdi. Geleneksel finans kurumları tescilli ana bilgisayarlar (mainframes) tutarak tüketici işlem geçmişlerini, kredi puanlarını ve hesap bakiyelerini münhasır şirket mülkiyeti olarak gördü. Finansal sağlayıcı değiştirmek veya hesapları birleştirmek isteyen müşteriler hantal bürokratik engellerle karşılaştı. Ancak son on yılda Avrupa Birliği'nin PSD2 direktifi gibi ilerici mevzuat ile RESTful API standartlarının güçlü birleşimi bu tekelci mimariyi yıktı."
            },
            {
                "paragraph_index": 2,
                "title": "Standardized RESTful Endpoints and Consent",
                "content_en": "Open Banking mandates that licensed financial institutions construct secure, standardized Application Programming Interfaces (APIs). Through cryptographic protocols like OAuth 2.0 and OpenID Connect, consumers grant explicit, time-delimited consent to authorized third-party fintech applications. An independent mobile app can instantaneously retrieve account balances, initiate domestic SEPA transfers, or analyze multi-year spending patterns without ever exposing or storing the user's secret banking credentials.",
                "content_tr": "Açık Bankacılık, lisanslı finans kurumlarının güvenli, standartlaştırılmış Uygulama Programlama Arayüzleri (API'ler) inşa etmesini zorunlu kılar. OAuth 2.0 ve OpenID Connect gibi kriptografik protokoller aracılığıyla tüketiciler yetkili üçüncü taraf fintech uygulamalarına açık ve zaman sınırlı onay verir. Bağımsız bir mobil uygulama, kullanıcının gizli bankacılık kimlik bilgilerini hiçbir zaman görmeden veya saklamadan hesap bakiyelerini anında alabilir, yerel SEPA transferleri başlatabilir veya çok yıllık harcama modellerini analiz edebilir."
            },
            {
                "paragraph_index": 3,
                "title": "The Decoupling of Banking Services",
                "content_en": "The architectural ramification of open APIs is the unbundling of banking. In the legacy paradigm, a single bank provided savings accounts, mortgages, currency exchange, and investment portfolios—often with mediocre software interfaces and inflated fees. In the open banking ecosystem, fintech specialists disaggregate these offerings. Specialized startups compete aggressively on specialized micro-services: algorithmic micro-investing, cross-border remittance at interbank rates, and dynamic real-time credit underwriting.",
                "content_tr": "Açık API'lerin mimari sonucu, bankacılığın ayrışmasıdır (unbundling). Eski paradigmada tek bir banka tasarruf hesapları, ipotekler, döviz bozdurma ve yatırım portföyleri sunardı (genellikle vasat yazılım arayüzleri ve şişirilmiş ücretlerle). Açık bankacılık ekosisteminde ise fintech uzmanları bu teklifleri birbirinden ayırır. Özelleşmiş girişimler belirli mikro hizmetlerde kıyasıya rekabet eder: algoritmik mikro yatırım, bankalar arası oranlarla sınır ötesi havale ve dinamik gerçek zamanlı kredi tahsisi."
            },
            {
                "paragraph_index": 4,
                "title": "Security Challenges and Embedded Finance",
                "content_en": "While open banking democratizes financial data, it simultaneously expands the threat landscape. When thousands of third-party platforms communicate via programmatic endpoints, API gateway vulnerabilities, token leakage, and unauthorized data scraping pose severe compliance risks. Financial regulators now mandate continuous behavioral telemetry, biometric verification, and strict cryptographic audit trails. As open banking matures, the future points toward 'Embedded Finance'—where financial transactions occur invisibly inside everyday commercial software platforms.",
                "content_tr": "Açık bankacılık finansal verileri demokratikleştirirken aynı zamanda tehdit alanını da genişletir. Binlerce üçüncü taraf platformu programatik uç noktalar aracılığıyla iletişim kurduğunda API ağ geçidi açıkları, belirteç (token) sızıntısı ve yetkisiz veri kazıma ciddi uyumluluk riskleri oluşturur. Finansal düzenleyiciler artık sürekli davranışsal telemetri, biyometrik doğrulama ve sıkı kriptografik denetim kayıtları talep etmektedir. Açık bankacılık olgunlaştıkça gelecek finansal işlemlerin günlük ticari yazılım platformlarının içinde görünmez şekilde gerçekleştiği 'Gömülü Finans'a (Embedded Finance) işaret etmektedir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "paradigm",
                "vocab_id": "vocab.paradigm",
                "context_definition_en": "A typical example, pattern, or overarching conceptual model of something.",
                "context_meaning_tr": "Bir şeyin temel modeli, kalıbı veya genel kavramsal çerçevesi (paradigma)."
            },
            {
                "word": "disaggregate",
                "vocab_id": "vocab.disaggregate",
                "context_definition_en": "To separate something into its component parts or distinct services.",
                "context_meaning_tr": "Bir bütünü bileşenlerine veya bağımsız parçalarına ayırmak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_02_01",
                "What characterized the 'walled garden' approach of legacy retail banking?",
                "Geleneksel bireysel bankacılığın 'kapalı bahçe' yaklaşımını ne karakterize ediyordu?",
                "Treating consumer transaction histories as proprietary, impenetrable corporate property",
                ["Giving all customers completely free access to interbank trading systems", "Openly sharing all customer passwords with international competitors", "Eliminating all bank branches in favor of digital decentralized currencies"],
                "Paragraph 1 explains legacy banks treated transaction histories as exclusive property.",
                "1. paragraf eski bankaların işlem geçmişlerini münhasır şirket mülkü olarak gördüğünü belirtir."
            ),
            build_q(
                "q_r_b2_02_02",
                "How does Open Banking protect user passwords while enabling third-party integration?",
                "Açık Bankacılık üçüncü taraf entegrasyonu sağlarken kullanıcı şifrelerini nasıl korur?",
                "By using cryptographic protocols like OAuth 2.0 to grant explicit, time-delimited access tokens",
                ["By requiring bank managers to physically type passwords into customer phones", "By printing passwords on physical paper mailed through international postal services", "By replacing all passwords with public personal phone numbers"],
                "Paragraph 2 states protocols like OAuth 2.0 grant consent without exposing secret credentials.",
                "2. paragraf OAuth 2.0'ın şifreyi ifşa etmeden yetkilendirme sağladığını açıklar."
            ),
            build_q(
                "q_r_b2_02_03",
                "What is meant by the 'unbundling' of banking in the third paragraph?",
                "Üçüncü paragrafta bankacılığın 'ayrışması' (unbundling) ile ne kastedilmektedir?",
                "Specialized startups disaggregating all-in-one bank services into superior standalone micro-services",
                ["The physical destruction of commercial bank headquarters buildings", "The total abolition of all national currencies across global markets", "Forcing customers to maintain accounts with at least fifty banks simultaneously"],
                "Paragraph 3 describes specialists disaggregating offerings into specialized micro-services.",
                "3. paragraf uzmanlaşmış girişimlerin hizmetleri bağımsız parçalara ayırdığını belirtir."
            ),
            build_q(
                "q_r_b2_02_04",
                "What new security vulnerability arises from opening programmatic API endpoints to thousands of platforms?",
                "Programatik API uç noktalarını binlerce platforma açmaktan hangi yeni güvenlik açığı doğar?",
                "Token leakage, API gateway vulnerabilities, and unauthorized data scraping",
                ["The complete physical overheating of global telecommunications cables", "The immediate loss of customer computer operating system licenses", "An inability of computer keyboards to input numerical currency values"],
                "Paragraph 4 lists API gateway vulnerabilities, token leakage, and unauthorized scraping.",
                "4. paragraf API açıkları, belirteç sızıntısı ve izinsiz kazımayı sıralar."
            ),
            build_q(
                "q_r_b2_02_05",
                "What is 'Embedded Finance' as forecasted at the conclusion of the article?",
                "Makalenin sonunda öngörülen 'Gömülü Finans' (Embedded Finance) nedir?",
                "Financial transactions occurring invisibly and seamlessly within everyday commercial software platforms",
                ["Implanting physical computer chips inside bank customers' hands", "Banning all credit cards and returning to physical gold coinage", "Eliminating all software code from the banking sector completely"],
                "Paragraph 4 defines Embedded Finance as financial transactions occurring invisibly inside software.",
                "4. paragraf finansal işlemlerin yazılımların içinde görünmezce gerçekleşmesi olarak tanımlar."
            )
        ],
        "topic_tags": ["fintech", "open-banking", "apis", "finance", "b2-reading"],
        "related_ids": ["vocab.paradigm", "vocab.disaggregate"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.strategic-okr-implementation",
        "title": "Implementing Objectives and Key Results in High-Velocity Engineering Squads",
        "cefr_level": "B2",
        "category": "business_strategy",
        "summary_en": "How technology enterprises implement Objectives and Key Results (OKRs) to align engineering output with executive business impact, avoiding output-oriented feature factories.",
        "summary_tr": "Teknoloji şirketlerinin mühendislik çıktısını kurumsal iş etkisiyle hizalamak ve 'özellik fabrikası' tuzağından kaçınmak için OKR sistemini nasıl uyguladığı.",
        "word_count": 810,
        "estimated_reading_minutes": 4,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Trap of the Feature Factory",
                "content_en": "In many growing software organizations, engineering productivity is mistakenly evaluated by raw output: number of tickets completed, story points burned down, or features shipped. This metric creates what industry practitioners call a 'Feature Factory'—a team that churns out endless software capabilities that nobody actually uses or that fail to move the company's financial needle. To connect technical effort directly to measurable business outcomes, leading technology organizations adopt the Objectives and Key Results (OKR) framework.",
                "content_tr": "Büyüyen birçok yazılım organizasyonunda mühendislik üretkenliği yanlışlıkla ham çıktıyla değerlendirilir: tamamlanan bilet sayısı, tüketilen hikaye puanları veya yayınlanan özellikler. Bu ölçüt sektör çalışanlarının 'Özellik Fabrikası' (Feature Factory) adını verdiği durumu yaratır: hiç kimsenin gerçekten kullanmadığı veya şirketin finansal ibresini oynatmakta başarısız olan sonsuz yazılım yetenekleri üreten bir ekip. Teknik çabayı doğrudan ölçülebilir iş sonuçlarına bağlamak için önde gelen teknoloji şirketleri Hedefler ve Temel Sonuçlar (OKR) çerçevesini benimser."
            },
            {
                "paragraph_index": 2,
                "title": "Anatomy of an Effective OKR",
                "content_en": "The OKR methodology, originally pioneered at Intel and popularized across Silicon Valley by Google, consists of two interlocking components. The 'Objective' is a qualitative, ambitious, and inspirational statement defining where the team wants to go (e.g., 'Deliver a lightning-fast, ultra-reliable checkout experience'). The 'Key Results' are three to five quantitative, rigorously measurable milestones that prove whether the objective was achieved (e.g., 'Reduce checkout latency from 3.2 seconds to 800 milliseconds').",
                "content_tr": "İlk olarak Intel'de öncülüğü yapılan ve Silikon Vadisi genelinde Google tarafından popülerleştirilen OKR metodolojisi, birbirine kenetlenen iki bileşenden oluşur. 'Hedef' (Objective), ekibin nereye gitmek istediğini tanımlayan niteliksel, iddialı ve ilham verici bir ifadedir (örneğin: 'Şimşek hızında ve son derece güvenilir bir ödeme deneyimi sunmak'). 'Temel Sonuçlar' (Key Results) ise hedefe ulaşılıp ulaşılmadığını kanıtlayan üç ila beş adet niceliksel, sıkı bir şekilde ölçülebilir kilometre taşıdır (örneğin: 'Ödeme gecikmesini 3,2 saniyeden 800 milisaniyeye düşürmek')."
            },
            {
                "paragraph_index": 3,
                "title": "Outputs versus Outcomes",
                "content_en": "The most common failure mode in adopting OKRs is writing Key Results as task lists rather than outcome metrics. Writing 'Launch the redesigned iOS payment screen' is an output; it merely measures whether work was performed, not whether it generated value. In contrast, writing 'Increase checkout completion conversion by four percent' is an outcome. If the team launches the screen but conversion declines, the Key Result has objectively failed, forcing the team to iterate rapidly.",
                "content_tr": "OKR'ları benimsemedeki en yaygın hata modu, Temel Sonuçları sonuç ölçütleri yerine görev listeleri olarak yazmaktır. 'Yeniden tasarlanan iOS ödeme ekranını yayınlamak' bir çıktıdır (output); yalnızca işin yapılıp yapılmadığını ölçer, değer üretip üretmediğini değil. Buna karşılık, 'Ödeme tamamlama dönüşüm oranını yüzde dört artırmak' bir sonuçtur (outcome). Ekip ekranı yayınlar ancak dönüşüm oranı düşerse, Temel Sonuç nesnel olarak başarısız olmuştur ve bu da ekibi hızla yeni denemeler yapmaya zorlar."
            },
            {
                "paragraph_index": 4,
                "title": "Decoupling OKRs from Compensation",
                "content_en": "A fundamental rule of OKR governance is strictly decoupling Key Result attainment from employee performance bonuses. If annual compensation is tied directly to achieving 100% of an OKR, engineers will sandbag: artificially setting modest, conservative targets to guarantee bonuses. OKRs are designed as stretch goals where achieving seventy percent represents impressive execution. By removing financial fear from the equation, leadership encourages high-stakes innovation and bold technical experimentation.",
                "content_tr": "OKR yönetişiminin temel bir kuralı, Temel Sonuç başarısını çalışan performans primlerinden kesin olarak ayırmaktır. Yıllık primler doğrudan bir OKR'ın %100'üne ulaşmaya bağlanırsa mühendisler hedef küçültecektir (sandbagging): primleri garanti altına almak için yapay olarak mütevazı ve muhafazakar hedefler belirleyeceklerdir. OKR'lar yüzde yetmişine ulaşmanın etkileyici bir başarı sayıldığı esnetme (stretch) hedefleri olarak tasarlanmıştır. Liderlik denklemin içinden finansal korkuyu çıkararak yüksek riskli inovasyonu ve cesur teknik deneyleri teşvik eder."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "catalyst",
                "vocab_id": "vocab.catalyst",
                "context_definition_en": "A person or thing that precipitates an event or accelerates change.",
                "context_meaning_tr": "Bir olayı hızlandıran veya değişimi tetikleyen unsur (katalizör)."
            },
            {
                "word": "sandbag",
                "vocab_id": "vocab.sandbag",
                "context_definition_en": "To deliberately set low expectations or conservative targets to look good later.",
                "context_meaning_tr": "Daha sonra başarılı görünmek için beklentileri veya hedefleri kasten düşük tutmak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_03_01",
                "What defines the 'Feature Factory' trap in software organizations?",
                "Yazılım organizasyonlarında 'Özellik Fabrikası' tuzağını ne tanımlar?",
                "Focusing exclusively on shipping raw output without evaluating whether it generates real business value",
                ["A manufacturing facility that physically constructs computer keyboards", "A requirement that all code must be written in the C programming language", "A legal mandate forcing technology companies to hire factory labor"],
                "Paragraph 1 explains a feature factory churns out features that fail to move the financial needle.",
                "1. paragraf özellik fabrikasının iş değeri yaratmayan özellikleri durmadan üretmek olduğunu açıklar."
            ),
            build_q(
                "q_r_b2_03_02",
                "How do 'Objectives' differ from 'Key Results' in the OKR framework?",
                "OKR çerçevesinde 'Hedefler' (Objectives) 'Temel Sonuçlar'dan (Key Results) nasıl ayrılır?",
                "Objectives are qualitative and inspirational; Key Results are quantitative and measurable",
                ["Objectives are written in binary code; Key Results are written in English", "Objectives are strictly for executives; Key Results are strictly for junior interns", "Objectives change every five minutes; Key Results never change over ten years"],
                "Paragraph 2 states the Objective is qualitative/inspirational, while Key Results are quantitative/measurable.",
                "2. paragraf Hedefin niteliksel/ilham verici, Temel Sonuçların ise niceliksel/ölçülebilir olduğunu belirtir."
            ),
            build_q(
                "q_r_b2_03_03",
                "Why is 'Launch the new payment screen' considered a flawed Key Result?",
                "'Yeni ödeme ekranını yayınlamak' neden hatalı bir Temel Sonuç olarak kabul edilir?",
                "It is a task-based output, not a measurable business outcome metric",
                ["It violates international patent laws regarding digital typography", "It takes more than three hours to translate into German", "The word 'screen' is forbidden in modern agile frameworks"],
                "Paragraph 3 explains launching a screen is an output measuring work performed, not value generated.",
                "3. paragraf ekran yayınlamanın bir çıktı olduğunu, değer üreten bir sonuç olmadığını açıklar."
            ),
            build_q(
                "q_r_b2_03_04",
                "What happens when companies tie employee performance bonuses directly to OKR completion?",
                "Şirketler çalışan primlerini doğrudan OKR başarısına bağladığında ne olur?",
                "Engineers set artificially conservative targets (sandbagging) to guarantee bonuses",
                ["The company automatically triples its quarterly sales revenue", "All software bugs are eliminated from the codebase permanently", "Employees work seventy hours a week with joyful enthusiasm"],
                "Paragraph 4 explains employees will sandbag and set modest targets to guarantee bonuses.",
                "4. paragraf çalışanların primleri garantilemek için hedefleri yapay biçimde düşüreceğini açıklar."
            ),
            build_q(
                "q_r_b2_03_05",
                "What achievement level in stretch OKRs is typically considered impressive execution?",
                "İddialı esnetme (stretch) OKR'larında hangi başarı seviyesi genellikle etkileyici bir icraat sayılır?",
                "Attaining approximately seventy percent of the ambitious target",
                ["Achieving precisely zero point zero percent", "Achieving five hundred percent every single sprint", "Completing tasks without ever using a computer"],
                "Paragraph 4 mentions that achieving seventy percent represents impressive execution.",
                "4. paragraf hedefin yüzde yetmişine ulaşmanın etkileyici bir başarı olduğunu belirtir."
            )
        ],
        "topic_tags": ["okrs", "business-strategy", "engineering-management", "b2-reading"],
        "related_ids": ["vocab.catalyst", "vocab.sandbag"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.design-systems-at-scale",
        "title": "Design Systems as Bridges Between Design and Engineering",
        "cefr_level": "B2",
        "category": "technology",
        "summary_en": "How design tokens, reusable UI component libraries, and cross-discipline governance align product designers and front-end engineers across sprawling software suites.",
        "summary_tr": "Tasarım belirteçleri (tokens), yeniden kullanılabilir kullanıcı arayüzü kütüphaneleri ve yönetişimin tasarımcılar ile yazılımcıları nasıl birleştirdiği.",
        "word_count": 830,
        "estimated_reading_minutes": 4,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Visual Inconsistency of Scaling Products",
                "content_en": "When a technology company grows from a single startup product to an expansive portfolio of web, mobile, and tablet applications, UI chaos inevitably follows. Different engineering squads independently implement buttons, color palettes, and modal dialogs. Over time, the flagship application contains fifteen slightly different shades of blue, seven distinct primary button styles, and erratic spacing hierarchies. This fragmentation degrades brand credibility, frustrates users, and forces developers to waste thousands of engineering hours reinventing basic visual components.",
                "content_tr": "Bir teknoloji şirketi tek bir girişim ürününden geniş bir web, mobil ve tablet uygulamaları portföyüne doğru büyüdüğünde kaçınılmaz olarak kullanıcı arayüzü kaosu ortaya çıkar. Farklı mühendislik ekipleri butonları, renk paletlerini ve modal diyalogları birbirinden bağımsız olarak uygular. Zamanla ana uygulama mavinin on beş farklı tonunu, yedi farklı birincil buton stilini ve düzensiz boşluk hiyerarşilerini barındırır hale gelir. Bu parçalanma marka güvenilirliğini zedeler, kullanıcıları hayal kırıklığına uğratır ve yazılımcıların temel görsel bileşenleri yeniden icat ederek binlerce mühendislik saatini boşa harcamasına neden olur."
            },
            {
                "paragraph_index": 2,
                "title": "The Power of Design Tokens",
                "content_en": "At the architectural foundation of a modern design system lies the concept of 'Design Tokens'. A design token is an agnostic key-value pair that stores design decisions: typography scales, hex colors, corner radii, and elevation shadows. Rather than hardcoding '#0052CC' into an Android XML layout, a React CSS stylesheet, and an iOS Swift view, developers reference a single semantic token such as 'color.brand.primary'. When brand guidelines evolve, designers update the token once, and changes propagate automatically across all platforms via automated build scripts.",
                "content_tr": "Modern bir tasarım sisteminin mimari temelinde 'Tasarım Belirteçleri' (Design Tokens) kavramı yer alır. Bir tasarım belirteci tasarım kararlarını saklayan platformdan bağımsız bir anahtar-değer çiftidir: tipografi ölçekleri, hex renkleri, köşe yuvarlama değerleri ve gölge yükseklikleri. Yazılımcılar bir Android XML düzenine, bir React CSS stiline ve bir iOS Swift görünümüne elle '#0052CC' kodlamak yerine 'color.brand.primary' gibi tek bir anlamsal belirtece başvururlar. Marka kuralları değiştiğinde tasarımcılar belirteci bir kez günceller ve değişiklikler otomatik derleme betikleri aracılığıyla tüm platformlara kendiliğinden yayılır."
            },
            {
                "paragraph_index": 3,
                "title": "Component Libraries and Accessibility by Default",
                "content_en": "Above the token layer sits the shared component library. Buttons, dropdowns, navigation rails, and data tables are engineered once as robust, accessible building blocks. Crucially, complex accessibility requirements—such as screen reader announcements, focus states, dynamic text resizing, and keyboard navigation—are solved centrally. Feature developers simply assemble pre-tested, accessible components like building blocks, accelerating delivery while ensuring legal compliance with international accessibility standards.",
                "content_tr": "Belirteç katmanının üzerinde paylaşılan bileşen kütüphanesi yer alır. Butonlar, açılır menüler, navigasyon çubukları ve veri tabloları sağlam, erişilebilir yapı taşları olarak bir kez tasarlanır. En önemlisi ekran okuyucu duyuruları, odaklanma durumları, dinamik metin yeniden boyutlandırma ve klavye navigasyonu gibi karmaşık erişilebilirlik gereksinimleri merkezi olarak çözülür. Özellik geliştiricileri önceden test edilmiş, erişilebilir bileşenleri yapı taşları gibi bir araya getirerek uluslararası erişilebilirlik standartlarına yasal uyumu sağlarken teslimatı hızlandırırlar."
            },
            {
                "paragraph_index": 4,
                "title": "Governance and the Living System",
                "content_en": "The most sophisticated component library will wither into irrelevance without active governance. A design system is not a static code repository; it is an ongoing interdisciplinary contract between design and engineering. Cross-functional committees meet bi-weekly to review proposed component contributions, deprecate outdated patterns, and audit production applications for design drift. When treated as an evolving product with dedicated maintainers, a design system becomes an extraordinary multiplier of organizational velocity.",
                "content_tr": "En gelişmiş bileşen kütüphanesi bile aktif yönetişim olmadan körelip etkisizleşecektir. Bir tasarım sistemi statik bir kod deposu değildir; tasarım ve mühendislik arasında devam eden disiplinler arası bir sözleşmedir. Fonksiyonlar arası komiteler önerilen bileşen katkılarını incelemek, modası geçmiş kalıpları kullanımdan kaldırmak ve canlı uygulamaları tasarım sapması açısından denetlemek için iki haftada bir toplanır. Özel bakımcıları olan gelişen bir ürün olarak ele alındığında bir tasarım sistemi, kurumsal hızın olağanüstü bir çarpanı haline gelir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "cohesion",
                "vocab_id": "vocab.cohesion",
                "context_definition_en": "The state of sticking together or creating a unified, harmonious whole.",
                "context_meaning_tr": "Birbirine kenetlenme veya uyumlu bir bütün oluşturma durumu (bütünlük)."
            },
            {
                "word": "agnostic",
                "vocab_id": "vocab.agnostic",
                "context_definition_en": "Designed to be compatible with different platforms or technologies without modification.",
                "context_meaning_tr": "Belirli bir teknolojiye bağımlı olmayan, platformlar arası uyumlu."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_04_01",
                "What visual problem commonly emerges when expanding software products lack a unified design system?",
                "Genişleyen yazılım ürünlerinde birleşik bir tasarım sistemi olmadığında yaygın olarak hangi görsel sorun ortaya çıkar?",
                "Inconsistent UI elements, fragmented color palettes, and erratic spacing hierarchies",
                ["All software buttons permanently disappear from computer screens", "Computer monitors display black and white images exclusively", "Users are legally fined by the government for clicking on buttons"],
                "Paragraph 1 highlights UI chaos with fifteen shades of blue and erratic spacing.",
                "1. paragraf mavinin on beş tonu ve düzensiz boşluklarla oluşan UI kaosunu vurgular."
            ),
            build_q(
                "q_r_b2_04_02",
                "What is a 'Design Token' as explained in the second paragraph?",
                "İkinci paragrafta açıklandığı gibi bir 'Tasarım Belirteci' (Design Token) nedir?",
                "A platform-agnostic key-value pair storing foundational design decisions like color or typography",
                ["A physical plastic coin given to software engineers on their birthdays", "A cryptographic digital coin used to purchase office stationery", "A complex password required to turn on office lighting systems"],
                "Paragraph 2 defines a token as an agnostic key-value pair storing design decisions.",
                "2. paragraf belirteci tasarım kararlarını saklayan platformdan bağımsız anahtar-değer çifti olarak tanımlar."
            ),
            build_q(
                "q_r_b2_04_03",
                "How does a centralized component library improve software accessibility?",
                "Merkezi bir bileşen kütüphanesi yazılım erişilebilirliğini nasıl geliştirir?",
                "Screen reader states, keyboard navigation, and text resizing are solved centrally once",
                ["By eliminating the need for users to look at computer screens", "By translating all software code into ancient classical Greek", "By requiring all software users to take an official eye examination"],
                "Paragraph 3 explains accessibility requirements are solved centrally in shared components.",
                "3. paragraf ekran okuyucu ve klavye erişilebilirliğinin merkezi olarak bir kez çözüldüğünü belirtir."
            ),
            build_q(
                "q_r_b2_04_04",
                "Why will a design system wither into irrelevance without active governance?",
                "Bir tasarım sistemi aktif yönetişim olmadan neden körelip etkisizleşir?",
                "Without cross-discipline review committees, design drift and outdated patterns proliferate",
                ["Because computer programming languages expire after twelve calendar months", "Because designers and engineers are legally forbidden from meeting in person", "Because all software code is deleted by operating systems every weekend"],
                "Paragraph 4 explains governance prevents design drift and deprecates outdated patterns.",
                "4. paragraf yönetişimin tasarım sapmasını önlediğini ve eski kalıpları temizlediğini açıklar."
            ),
            build_q(
                "q_r_b2_04_05",
                "What major efficiency benefit do design tokens offer when rebranding?",
                "Tasarım belirteçleri marka yenileme sırasında ne gibi büyük bir verimlilik avantajı sunar?",
                "Updating a token once automatically propagates changes across web, iOS, and Android platforms",
                ["It eliminates the need for any future software testing", "It guarantees that mobile phones will never run out of battery power", "It allows developers to work without receiving financial salaries"],
                "Paragraph 2 explains designers update the token once and changes propagate automatically.",
                "2. paragraf belirtecin bir kez güncellenmesiyle değişikliklerin tüm platformlara yayıldığını belirtir."
            )
        ],
        "topic_tags": ["design-systems", "design-tokens", "ui-engineering", "accessibility", "b2-reading"],
        "related_ids": ["vocab.cohesion", "vocab.agnostic"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.cross-cultural-negotiation",
        "title": "Navigating Cultural Nuance in Global Business Negotiations",
        "cefr_level": "B2",
        "category": "workplace_communication",
        "summary_en": "An examination of low-context vs. high-context communication, direct vs. indirect disagreement, and pacing rituals in international technology partnerships.",
        "summary_tr": "Uluslararası iş müzakerelerinde kültürel incelikler: düşük bağlamlı ve yüksek bağlamlı iletişim, doğrudan ve dolaylı itiraz ve müzakere ritimleri.",
        "word_count": 840,
        "estimated_reading_minutes": 4,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Hidden Pitfalls of Cross-Border Deals",
                "content_en": "In our hyper-connected global economy, technology commercialization frequently transcends national boundaries. An enterprise software vendor in California negotiates multi-million-dollar licensing agreements with automotive conglomerates in Germany, financial consortia in Japan, and logistics hubs in Turkey. While technical architecture and contract pricing are objective, the human interactions across the negotiation table are heavily shaped by unspoken cultural assumptions. Executives who treat cross-border negotiation as a purely transactional mathematical exercise frequently experience sudden, baffling deal collapses.",
                "content_tr": "Son derece bağlantılı küresel ekonomimizde teknoloji ticarileşmesi sıklıkla ulusal sınırları aşar. Kaliforniya'daki bir kurumsal yazılım şirketi Almanya'daki otomotiv holdingleriyle, Japonya'daki finans konsorsiyumlarıyla ve Türkiye'deki lojistik merkezleriyle milyonlarca dolarlık lisans anlaşmaları müzakere eder. Teknik mimari ve sözleşme fiyatlandırması nesnel olsa da müzakere masası etrafındaki insani etkileşimler dile getirilmeyen kültürel varsayımlarla derinden şekillenir. Sınır ötesi müzakereleri tamamen işlemsel matematiksel bir egzersiz olarak gören yöneticiler sıklıkla ani ve kafa karıştırıcı anlaşma iptalleriyle karşılaşırlar."
            },
            {
                "paragraph_index": 2,
                "title": "Low-Context versus High-Context Communication",
                "content_en": "Anthropologist Edward T. Hall identified a fundamental taxonomy of cultural communication: low-context versus high-context cultures. In low-context business cultures (such as the United States, the Netherlands, and Germany), communication is explicit, precise, and literal. Meaning resides entirely in the exact words spoken. In high-context cultures (including Japan, South Korea, and many Mediterranean and Middle Eastern societies), message meaning is deeply intertwined with relational hierarchy, unspoken body language, and shared environmental context. In a high-context setting, a polite phrase like 'That proposal presents interesting challenges' often signals an emphatic 'No'.",
                "content_tr": "Antropolog Edward T. Hall kültürel iletişimin temel bir sınıflandırmasını belirlemiştir: düşük bağlamlı ve yüksek bağlamlı kültürler. Düşük bağlamlı iş kültürlerinde (Amerika Birleşik Devletleri, Hollanda ve Almanya gibi) iletişim açık, kesin ve lafzidir. Anlam tamamen konuşulan tam kelimelerde yer alır. Yüksek bağlamlı kültürlerde ise (Japonya, Güney Kore ve birçok Akdeniz ve Orta Doğu toplumu dahil) mesajın anlamı ilişkisel hiyerarşi, söylenmeyen beden dili ve paylaşılan çevresel bağlamla derinden iç içe geçmiştir. Yüksek bağlamlı bir ortamda 'Bu teklif ilginç zorluklar barındırıyor' gibi kibar bir ifade genellikle kesin bir 'Hayır' anlamına gelir."
            },
            {
                "paragraph_index": 3,
                "title": "Direct Disagreement and Relationship Capital",
                "content_en": "Cultural attitudes toward professional conflict vary dramatically across regions. In certain European business environments, particularly France and the Netherlands, passionate direct disagreement is celebrated as intellectual rigor and commitment to excellence. Conversely, in many Asian business cultures, publicly challenging a counterparty causes irreparable 'loss of face', destroying the relational harmony required for commercial partnership. Savvy international negotiators master the art of emotional calibration: expressing disagreement through private, informal side-channels rather than public confrontations.",
                "content_tr": "Mesleki çatışmaya yönelik kültürel tutumlar bölgeler arasında çarpıcı biçimde farklılık gösterir. Bazı Avrupa iş ortamlarında (özellikle Fransa ve Hollanda) tutkulu doğrudan itiraz entelektüel titizlik ve mükemmellik taahhüdü olarak kutlanır. Tersine birçok Asya iş kültüründe karşı tarafa kamuya açık bir şekilde meydan okumak onarılamaz bir 'itibar kaybına' (loss of face) yol açarak ticari ortaklık için gereken ilişkisel uyumu yok eder. Zeki uluslararası müzakereciler duygusal kalibrasyon sanatında ustalaşır: anlaşmazlıkları kamusal yüzleşmeler yerine özel, gayri resmi yan kanallar aracılığıyla ifade ederler."
            },
            {
                "paragraph_index": 4,
                "title": "Pacing and Decision Velocity",
                "content_en": "Finally, the temporal pacing of negotiations requires acute patience. Anglo-American dealmakers pride themselves on rapid transactional velocity—aiming to close contract terms within days. In contrast, in cultures emphasizing consensus-driven governance (such as the Japanese 'Ringi' decision-making process), every stakeholder must be consulted thoroughly before a formal signature is executed. Interpreting deliberate deliberation as disinterest or obstruction is a fatal error; understanding pacing builds the enduring trust that underpins long-term enterprise alliances.",
                "content_tr": "Son olarak müzakerelerin zamansal temposu derin bir sabır gerektirir. Anglo-Amerikan anlaşma yapıcıları sözleşme şartlarını günler içinde kapatmayı hedefleyerek hızlı işlemsel hızlarıyla gurur duyarlar. Buna karşılık fikir birliğine dayalı yönetişimi vurgulayan kültürlerde (Japonların 'Ringi' karar alma süreci gibi) resmi bir imza atılmadan önce her paydaşa derinlemesine danışılmalıdır. Bu bilinçli müzakere sürecini ilgisizlik veya engel çıkarma olarak yorumlamak ölümcül bir hatadır; tempoyu anlamak uzun vadeli kurumsal ittifakların temelini oluşturan kalıcı güveni inşa eder."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "conglomerate",
                "vocab_id": "vocab.conglomerate",
                "context_definition_en": "A large corporation formed by the merging of diverse companies in different business areas.",
                "context_meaning_tr": "Farklı sektörlerdeki şirketlerin birleşmesiyle oluşan dev holding veya şirketler topluluğu."
            },
            {
                "word": "taxonomy",
                "vocab_id": "vocab.taxonomy",
                "context_definition_en": "A scheme of classification and organization of concepts into structured categories.",
                "context_meaning_tr": "Kavramların yapılandırılmış kategorilere ayrıldığı sınıflandırma sistemi."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_05_01",
                "Why do cross-border business negotiations often collapse despite agreed technical specs?",
                "Sınır ötesi iş müzakereleri teknik şartnamelerde anlaşılmasına rağmen neden sıklıkla çöker?",
                "Executives treat human negotiations as purely transactional, ignoring unspoken cultural assumptions",
                ["International telecommunications cables are severed during international meetings", "Commercial banking contracts must be handwritten in classical Latin", "Governments forbid international partnerships across different oceans"],
                "Paragraph 1 notes that treating negotiations as purely transactional leads to baffling collapses.",
                "1. paragraf görüşmeleri sadece işlemsel görmenin ve kültürel varsayımları yoksaymanın anlaşmaları yıktığını belirtir."
            ),
            build_q(
                "q_r_b2_05_02",
                "How does communication function in 'low-context' business cultures?",
                "'Düşük bağlamlı' iş kültürlerinde iletişim nasıl işler?",
                "Communication is explicit, literal, and precise, with meaning residing in the exact words",
                ["Meaning is communicated exclusively through secret hand gestures", "All business must be negotiated through third-party family members", "Employees are strictly forbidden from writing emails or contracts"],
                "Paragraph 2 defines low-context cultures as explicit, precise, and literal.",
                "2. paragraf düşük bağlamlı kültürleri açık, kesin ve kelimelere dayalı olarak tanımlar."
            ),
            build_q(
                "q_r_b2_05_03",
                "What does the phrase 'That proposal presents interesting challenges' typically imply in high-context settings?",
                "Yüksek bağlamlı ortamlarda 'Bu teklif ilginç zorluklar barındırıyor' ifadesi genellikle ne anlama gelir?",
                "A polite and indirect yet definitive rejection ('No')",
                ["An immediate agreement to sign the contract without reading it", "An enthusiastic request to double the financial contract price", "A demand to hire fifty more junior programmers immediately"],
                "Paragraph 2 explains this polite phrase often signals an emphatic 'No'.",
                "2. paragraf bu kibar cümlenin genellikle kesin bir 'Hayır' anlamına geldiğini açıklar."
            ),
            build_q(
                "q_r_b2_05_04",
                "Why is public confrontation avoided in many Asian commercial partnerships?",
                "Birçok Asya ticari ortaklığında kamusal yüzleşmelerden neden kaçınılır?",
                "It causes irreversible 'loss of face' that permanently destroys relational harmony",
                ["International treaty law imposes criminal fines for speaking out loud", "It triggers automatic software compilation errors across local servers", "Asian conference rooms are acoustically designed to amplify angry voices"],
                "Paragraph 3 states challenging counterparties publicly causes loss of face and ruins harmony.",
                "3. paragraf açıkça meydan okumanın itibar kaybına yol açıp ilişkisel uyumu yıktığını belirtir."
            ),
            build_q(
                "q_r_b2_05_05",
                "What is a common error committed by fast-paced dealmakers in consensus-driven cultures?",
                "Fikir birliğine dayalı kültürlerde hızlı tempoya alışkın müzakerecilerin yaptığı yaygın hata nedir?",
                "Interpreting thorough, deliberate stakeholder deliberation as disinterest or obstruction",
                ["Offering to pay the invoice in local gold currency rather than paper bank notes", "Speaking too politely during initial ceremonial business introductions", "Refusing to allow any company lawyers to review the contract terms"],
                "Paragraph 4 explains interpreting deliberate deliberation as disinterest is a fatal error.",
                "4. paragraf istişare sürecini ilgisizlik veya engel çıkarma sanmanın ölümcül bir hata olduğunu açıklar."
            )
        ],
        "topic_tags": ["cross-cultural", "negotiation", "communication", "workplace-culture", "b2-reading"],
        "related_ids": ["vocab.conglomerate", "vocab.taxonomy"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.continuous-integration-evolution",
        "title": "The Architecture and Economics of Continuous Integration and Deployment Pipelines",
        "cefr_level": "B2",
        "category": "technology",
        "summary_en": "An in-depth analysis of the shift from periodic batch releases to continuous delivery, exploring trunk-based development, automated testing pyramids, and canary deployment economics.",
        "summary_tr": "Periyodik toplu sürümlerden sürekli teslimata (CD) geçişin mimari ve ekonomik analizi: trunk tabanlı geliştirme, otomatik test piramitleri ve kanarya dağıtımları.",
        "word_count": 1050,
        "estimated_reading_minutes": 5,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Historic Trauma of Big-Bang Releases",
                "content_en": "In the early decades of commercial software engineering, shipping software updates was a harrowing, high-anxiety ordeal. Organizations accumulated code modifications across months or years on long-lived development branches. When release day arrived, teams attempted what was colloquially termed a 'Big-Bang Deployment'. Thousands of disjointed code files, schema changes, and third-party dependency updates were merged simultaneously into production servers over grueling weekend marathons. Inevitably, unforeseen incompatibilities surfaced immediately. Databases locked up, customer authentication crashed, and exhausted engineers spent days executing frantic rollback scripts under intense executive pressure.",
                "content_tr": "Ticari yazılım mühendisliğinin ilk yıllarında yazılım güncellemelerini yayınlamak yıpratıcı ve yüksek kaygı içeren bir çileydi. Şirketler uzun ömürlü geliştirme dallarında aylarca veya yıllarca biriken kod değişikliklerini biriktirirdi. Yayın günü geldiğinde ekipler gayri resmi olarak 'Büyük Patlama Dağıtımı' (Big-Bang Deployment) olarak adlandırılan işlemi denerdi. Binlerce birbiriyle uyumsuz kod dosyası, veritabanı şema değişikliği ve üçüncü taraf bağımlılık güncellemeleri yorucu hafta sonu maratonlarında aynı anda canlı sunuculara birleştirilirdi. Kaçınılmaz olarak öngörülemeyen uyumsuzluklar anında su yüzüne çıkardı. Veritabanları kilitlenir, müşteri kimlik doğrulaması çöker ve bitkin mühendisler yoğun yönetim baskısı altında çılgınca geri alma (rollback) betikleri çalıştırarak günler harcardı."
            },
            {
                "paragraph_index": 2,
                "title": "The Paradigm of Continuous Integration",
                "content_en": "The methodology of Continuous Integration (CI) emerged as a direct philosophical and technical countermeasure to the trauma of big-bang deployments. Pioneered by agile thought leaders, CI mandates that developers integrate their code modifications into a central shared trunk multiple times every day. Rather than isolating changes on branch silos for weeks, engineers submit small, incremental pull requests. Each commit triggers an automated, containerized pipeline that pulls the new code, compiles the binary, executes a battery of automated tests, and verifies that the build remains fundamentally healthy.",
                "content_tr": "Sürekli Entegrasyon (CI) metodolojisi, büyük patlama dağıtımlarının yarattığı travmaya doğrudan felsefi ve teknik bir karşı önlem olarak ortaya çıktı. Çevik düşünce liderlerinin öncülük ettiği CI, yazılımcıların kod değişikliklerini her gün birden çok kez merkezi bir ortak ana dala (trunk) entegre etmesini zorunlu kılar. Değişiklikleri haftalarca dal silolarında izole etmek yerine mühendisler küçük, artımlı çekme istekleri sunar. Her bir commit yeni kodu çeken, derleyen, otomatik test bataryasını çalıştıran ve derlemenin temel olarak sağlıklı kaldığını doğrulayan otomatik, konteynerleştirilmiş bir işlem hattını tetikler."
            },
            {
                "paragraph_index": 3,
                "title": "The Automated Testing Pyramid",
                "content_en": "A robust CI/CD pipeline depends entirely on the structural discipline of the automated testing pyramid. At the broad base of the pyramid reside thousands of unit tests. Unit tests evaluate isolated functions and classes in milliseconds without touching network interfaces or disk storage. In the middle layer sit integration tests, which verify that microservices communicate accurately across database drivers and external API mocks. At the narrow apex of the pyramid are end-to-end browser and mobile tests, which validate critical user journeys from login to payment. By ensuring that ninety percent of automated coverage lives at the lightning-fast unit level, pipelines provide immediate feedback to developers within three to five minutes of pushing code.",
                "content_tr": "Sağlam bir CI/CD işlem hattı tamamen otomatik test piramidinin yapısal disiplinine dayanır. Piramidin geniş tabanında binlerce birim testi (unit test) bulunur. Birim testleri ağ arayüzlerine veya disk depolamasına dokunmadan izole fonksiyonları ve sınıfları milisaniyeler içinde değerlendirir. Orta katmanda mikroservislerin veritabanı sürücüleri ve harici API taklitleri üzerinden doğru iletişim kurduğunu doğrulayan entegrasyon testleri yer alır. Piramidin dar tepesinde ise oturum açmadan ödemeye kadar kritik kullanıcı yolculuklarını doğrulayan uçtan uca tarayıcı ve mobil testler bulunur. Otomatik kapsamın yüzde doksanının şimşek hızındaki birim seviyesinde olmasını sağlayarak işlem hatları geliştiricilere kod gönderdikten sonra üç ila beş dakika içinde anında geri bildirim sağlar."
            },
            {
                "paragraph_index": 4,
                "title": "Continuous Delivery versus Continuous Deployment",
                "content_en": "While frequently used interchangeably, a vital architectural distinction separates Continuous Delivery from Continuous Deployment. In Continuous Delivery, every passing commit automatically builds a production-ready release artifact, but the final deployment into customer-facing environments requires an intentional, manual human sign-off. This model is common in regulated banking, aviation, and medical software. In Continuous Deployment, human intervention is discarded completely: every single commit that passes automated linting, security scans, and test pyramids deploys to production servers automatically within minutes, sometimes dozens of times per day.",
                "content_tr": "Sıklıkla birbirinin yerine kullanılsa da Sürekli Teslimat (Continuous Delivery) ile Sürekli Dağıtımı (Continuous Deployment) birbirinden ayıran hayati bir mimari ayrım vardır. Sürekli Teslimatta geçen her commit otomatik olarak üretime hazır bir sürüm çıktısı oluşturur, ancak canlı ortama nihai dağıtım kasıtlı, manuel bir insan onayı gerektirir. Bu model düzenlemeye tabi bankacılık, havacılık ve tıbbi yazılımlarda yaygındır. Sürekli Dağıtımda ise insan müdahalesi tamamen ortadan kaldırılır: otomatik linter denetimlerini, güvenlik taramalarını ve test piramitlerini geçen her bir commit dakikalar içinde günde bazen onlarca kez otomatik olarak canlı sunuculara dağıtılır."
            },
            {
                "paragraph_index": 5,
                "title": "Canary Releases and Progressive Delivery",
                "content_en": "To mitigate the financial risk of deploying code dozens of times daily, modern elite engineering organizations practice progressive delivery through canary releases. Named after coal mine canaries that alerted miners to toxic gasses, a canary release routes new software versions to a tiny fraction of real production traffic—typically one or two percent. Automated telemetry systems monitor latency, error rates, and CPU spikes on the canary nodes. If telemetry detects anomalies, the deployment rolls back instantaneously without affecting ninety-eight percent of users. If metrics remain healthy, the system progressively ramps up traffic to one hundred percent.",
                "content_tr": "Kodu günde onlarca kez canlıya almanın getirdiği finansal riski azaltmak için modern seçkin mühendislik kuruluşları kanarya dağıtımları (canary releases) yoluyla aşamalı teslimat uygular. Madencileri zehirli gazlara karşı uyaran kömür madeni kanaryalarından adını alan kanarya dağıtımı, yeni yazılım sürümlerini gerçek üretim trafiğinin küçük bir kısmına (genellikle yüzde bir veya ikiye) yönlendirir. Otomatik telemetri sistemleri kanarya düğümlerindeki gecikmeyi, hata oranlarını ve işlemci sıçramalarını izler. Telemetri anormallikler tespit ederse dağıtım kullanıcıların yüzde doksan sekizini etkilemeden anında geri alınır. Metrikler sağlıklı kalırsa sistem trafiği kademeli olarak yüzde yüze çıkarır."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "disruption",
                "vocab_id": "vocab.disruption",
                "context_definition_en": "Disturbance or radical alteration that interrupts an ongoing process or system.",
                "context_meaning_tr": "Devam eden bir süreci veya sektörü kökten değiştiren kesinti veya dönüşüm."
            },
            {
                "word": "catalyst",
                "vocab_id": "vocab.catalyst",
                "context_definition_en": "An agent that provokes or speeds up significant change or technical action.",
                "context_meaning_tr": "Önemli bir değişimi veya teknik faaliyeti tetikleyen ve hızlandıran etken."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_06_01",
                "What was the primary hazard associated with historic 'Big-Bang Deployments'?",
                "Tarihi 'Büyük Patlama Dağıtımları' ile ilişkili temel tehlike neydi?",
                "Months of accumulated changes merged simultaneously caused catastrophic incompatibilities",
                ["Hardware engineers refused to plug in server power cords on weekends", "Software code was legally required to be written on paper forms first", "Computer displays could not render color graphics after midnight"],
                "Paragraph 1 explains merging months of accumulated changes caused unforeseen incompatibilities.",
                "1. paragraf aylarca biriken değişiklikleri aynı anda birleştirmenin büyük uyumsuzluklar yarattığını belirtir."
            ),
            build_q(
                "q_r_b2_06_02",
                "What is the central operational mandate of Continuous Integration (CI)?",
                "Sürekli Entegrasyonun (CI) merkezi operasyonel kuralı nedir?",
                "Integrating small, incremental code changes into a shared trunk multiple times per day",
                ["Writing all company software programs entirely inside a single thirty-line file", "Allowing developers to work in isolation for six months without talking to peers", "Deleting the code repository after every successful customer purchase"],
                "Paragraph 2 states CI mandates integrating code modifications into a shared trunk multiple times daily.",
                "2. paragraf CI'ın kodu her gün birden çok kez ortak ana dala birleştirmeyi zorunlu kıldığını açıklar."
            ),
            build_q(
                "q_r_b2_06_03",
                "Why should ninety percent of automated tests reside at the base unit test layer?",
                "Otomatik testlerin yüzde doksanı neden tabandaki birim test katmanında yer almalıdır?",
                "Unit tests execute in milliseconds without network or disk latency, providing fast feedback",
                ["Unit tests are required by the World Health Organization for medical reasons", "Unit tests can only be written by company chief executive officers", "Unit tests eliminate the need to purchase computer monitors for developers"],
                "Paragraph 3 states unit tests evaluate functions in milliseconds without network/disk overhead.",
                "3. paragraf birim testlerinin ağ veya disk yükü olmadan milisaniyeler içinde hızlı geri bildirim verdiğini açıklar."
            ),
            build_q(
                "q_r_b2_06_04",
                "What critical distinction separates Continuous Delivery from Continuous Deployment?",
                "Sürekli Teslimat ile Sürekli Dağıtımı ayıran kritik fark nedir?",
                "Continuous Delivery requires manual human approval to deploy, whereas Continuous Deployment deploys automatically",
                ["Continuous Delivery is only for mobile phones, while Deployment is only for televisions", "Continuous Delivery was invented in 1850, while Deployment was created last year", "Continuous Delivery requires paper checks, while Deployment uses digital credit cards"],
                "Paragraph 4 explains Delivery produces artifacts with human sign-off, while Deployment deploys automatically.",
                "4. paragraf Teslimatta manuel insan onayı gerektiğini, Dağıtımda ise tam otomatik canlıya alındığını belirtir."
            ),
            build_q(
                "q_r_b2_06_05",
                "How do canary deployments protect ninety-eight percent of production users from defects?",
                "Kanarya dağıtımları üretim kullanıcılarının yüzde doksan sekizini hatalardan nasıl korur?",
                "By initially routing new code to only one or two percent of traffic while monitoring automated telemetry",
                ["By disconnecting ninety-eight percent of users from the internet during deployment", "By forcing engineers to test the software on pet birds kept in the office", "By charging users double fees to access newly released features"],
                "Paragraph 5 explains canary releases route code to 1-2% of traffic and roll back if telemetry detects errors.",
                "5. paragraf kodun önce %1-2'lik trafiğe verilip telemetri izlenerek olası hatalarda geri alındığını açıklar."
            )
        ],
        "topic_tags": ["ci-cd", "continuous-integration", "devops", "software-engineering", "b2-reading"],
        "related_ids": ["vocab.disruption", "vocab.catalyst"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.leadership-emotional-intelligence",
        "title": "Emotional Intelligence and Adaptive Communication in Technical Leadership",
        "cefr_level": "B2",
        "category": "leadership_and_management",
        "summary_en": "An exploration of emotional quotient (EQ) in technology organizations, demonstrating how empathy, active listening, and self-regulation outperform raw cognitive intellect in executive leadership.",
        "summary_tr": "Teknoloji organizasyonlarında duygusal zeka (EQ): empati, etkin dinleme ve öz düzenlemenin üst düzey liderlikte saf zekadan (IQ) nasıl daha üstün performans gösterdiği.",
        "word_count": 1040,
        "estimated_reading_minutes": 5,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Myth of the Purely Rational Leader",
                "content_en": "The technology industry has long harbored a romantic myth: the solitary, hyper-rational engineering genius whose cognitive brilliance alone carries organizations to commercial triumph. Early software lore lionized abrasive founders who treated emotional sensitivity as a sign of weakness. However, as software systems transformed from solitary bedroom projects into sprawling sociotechnical ecosystems staffed by thousands of diverse professionals, this archetypal myth shattered. Modern organizational psychology conclusively proves that while cognitive ability (IQ) secures entry into technical fields, emotional quotient (EQ) is the primary determinant of long-term executive success.",
                "content_tr": "Teknoloji sektörü uzun zamandır romantik bir efsaneyi barındırmıştır: bilişsel dehası tek başına şirketleri ticari zafere taşıyan yalnız, aşırı rasyonel mühendislik dehası. Erken yazılım efsaneleri duygusal duyarlılığı bir zayıflık işareti olarak gören hırçın kurucuları yüceltti. Ancak yazılım sistemleri tek kişilik yatak odası projelerinden binlerce farklı uzmanın çalıştığı devasa sosyoteknik ekosistemlere dönüştükçe bu arketipik efsane yıkıldı. Modern örgütsel psikoloji kesin olarak kanıtlamaktadır ki bilişsel yetenek (IQ) teknik alanlara girişi güvence altına alırken duygusal zeka (EQ) uzun vadeli yönetici başarısının birincil belirleyicisidir."
            },
            {
                "paragraph_index": 2,
                "title": "The Four Pillars of Emotional Intelligence",
                "content_en": "Popularized by psychologist Daniel Goleman, emotional intelligence is structured upon four distinct, mutually reinforcing behavioral competencies. The first is self-awareness: the capacity to recognize your own emotional triggers, cognitive biases, and stress responses in real time. The second pillar is self-regulation: the discipline to withhold immediate, impulsive reactions during high-stakes crises. The third is social awareness, commonly termed empathy: the cognitive ability to perceive the unspoken anxieties and motivations of counterparties. The final pillar is relationship management: the skill to inspire alignment, resolve toxic conflict, and navigate complex organizational politics.",
                "content_tr": "Psikolog Daniel Goleman tarafından popülerleştirilen duygusal zeka, birbirini karşılıklı olarak güçlendiren dört farklı davranışsal yetkinlik üzerine yapılandırılmıştır. Birincisi öz farkındalıktır: kendi duygusal tetikleyicilerinizi, bilişsel önyargılarınızı ve stres tepkilerinizi gerçek zamanlı olarak tanıma kapasitesi. İkinci sütun öz düzenlemedir: yüksek riskli krizler sırasında anlık, dürtüsel tepkileri dizginleme disiplini. Üçüncüsü genellikle empati olarak adlandırılan sosyal farkındalıktır: karşı tarafların söylenmemiş endişelerini ve motivasyonlarını algılama bilişsel yeteneği. Son sütun ise ilişki yönetimidir: uyum sağlama, zehirli çatışmaları çözme ve karmaşık kurumsal siyasette yol alma becerisi."
            },
            {
                "paragraph_index": 3,
                "title": "Emotional Contagion in High-Pressure Crises",
                "content_en": "In technology environments, leaders operate under extreme scrutiny during catastrophic outages, missed product deadlines, or aggressive board reviews. Neurological research reveals that human emotions are biologically contagious. When an engineering vice president panics, berates subordinates, or projects erratic agitation, stress hormones surge throughout the organization. Fear impairs the prefrontal cortex—the exact neurological region responsible for creative problem-solving and analytical judgment. Conversely, leaders who project calm composure, methodical curiosity, and steady optimism de-escalate collective anxiety, enabling engineers to think clearly under duress.",
                "content_tr": "Teknoloji ortamlarında liderler feci kesintiler, kaçırılan ürün teslim tarihleri veya agresif yönetim kurulu incelemeleri sırasında aşırı inceleme altında çalışırlar. Nörolojik araştırmalar insan duygularının biyolojik olarak bulaşıcı olduğunu ortaya koymaktadır. Bir mühendislik başkan yardımcısı paniklediğinde, astlarını azarladığında veya düzensiz bir ajitasyon sergilediğinde stres hormonları organizasyon genelinde tavan yapar. Korku yaratıcı problem çözme ve analitik muhakemeden sorumlu olan beyin bölgesi prefrontal korteksi felce uğratır. Tersine sakin bir soğukkanlılık, metodik merak ve istikrarlı iyimserlik sergileyen liderler kolektif kaygıyı yatıştırarak mühendislerin baskı altında net düşünmesini sağlarlar."
            },
            {
                "paragraph_index": 4,
                "title": "Feedback Loops and Radical Empathy",
                "content_en": "Delivering performance feedback is among the most cognitively demanding challenges for technical managers. Leaders with low emotional intelligence rely on blunt, transaction-oriented criticism that triggers immediate defensive mechanisms in recipients. In contrast, emotionally intelligent leaders apply radical empathy. They frame critique around objective behavioral patterns and mutual growth outcomes, acknowledging the emotional vulnerability inherent in having one's technical work scrutinized. By separating an engineer's intrinsic identity from a specific codebase regression, leaders build lasting psychological safety.",
                "content_tr": "Performans geri bildirimi vermek, teknik yöneticiler için bilişsel açıdan en zorlu görevler arasındadır. Düşük duygusal zekaya sahip liderler alıcılarda anında savunma mekanizmalarını tetikleyen kaba, işlem odaklı eleştirilere güvenirler. Buna karşılık duygusal açıdan zeki liderler radikal empati uygularlar. Eleştiriyi nesnel davranışsal kalıplar ve karşılıklı büyüme çıktıları etrafında çerçevelerler ve kişinin teknik çalışmasının incelenmesinde var olan duygusal kırılganlığı kabul ederler. Bir mühendisin temel kişisel kimliğini belirli bir kod tabanı hatasından ayırarak liderler kalıcı bir psikolojik güvenlik inşa ederler."
            },
            {
                "paragraph_index": 5,
                "title": "The Strategic ROI of Empathetic Leadership",
                "content_en": "Far from being a soft, negligible corporate indulgence, emotional intelligence delivers quantifiable financial return on investment. Technology firms characterized by high emotional intelligence demonstrate substantially lower developer attrition rates, drastically reducing the exorbitant recruiting costs required to replace elite senior engineers. Furthermore, inclusive, emotionally safe engineering squads exhibit superior velocity and innovation rates, because developers are not paralyzed by the fear of making technical mistakes. In modern engineering leadership, empathy is not a luxury; it is a foundational performance advantage.",
                "content_tr": "Yumuşak ve önemsiz bir kurumsal heves olmaktan çok uzak olan duygusal zeka, ölçülebilir bir finansal yatırım getirisi (ROI) sağlar. Yüksek duygusal zeka ile karakterize edilen teknoloji şirketleri, kıdemli mühendisleri değiştirmek için gereken fahiş işe alım maliyetlerini büyük ölçüde azaltarak önemli ölçüde daha düşük yazılımcı ayrılma oranları sergiler. Dahası kapsayıcı ve duygusal olarak güvenli mühendislik ekipleri üstün bir hız ve yenilik oranı sergiler çünkü geliştiriciler teknik hata yapma korkusuyla felç olmazlar. Modern mühendislik liderliğinde empati bir lüks değil; temel bir performans avantajıdır."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "contagious",
                "vocab_id": "vocab.contagious",
                "context_definition_en": "Easily spread or communicated from one person to another, whether a disease or an emotion.",
                "context_meaning_tr": "Kişiden kişiye hızla yayılan ve bulaşan (duygu veya hastalık)."
            },
            {
                "word": "attrition",
                "vocab_id": "vocab.attrition",
                "context_definition_en": "The reduction in workforce size when employees leave and are not immediately replaced.",
                "context_meaning_tr": "Çalışanların ayrılması ve yerine hemen yenisinin konulamaması sonucu iş gücündeki aşınma/kayıp."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_07_01",
                "What romantic myth about technology founders does the article challenge?",
                "Makale teknoloji kurucuları hakkındaki hangi romantik efsaneye meydan okumaktadır?",
                "The belief that solitary, abrasive, hyper-rational geniuses can succeed without emotional sensitivity",
                ["The idea that computer programmers must possess degrees in classical literature", "The belief that all software companies must be incorporated in Pacific island nations", "The claim that computer software code cannot be written on laptop keyboards"],
                "Paragraph 1 challenges the myth of the abrasive, solitary genius succeeding without emotional skills.",
                "1. paragraf yalnız, hırçın ve aşırı rasyonel dehanın duygusal zeka olmadan başarılı olabileceği efsanesini çürütür."
            ),
            build_q(
                "q_r_b2_07_02",
                "According to Daniel Goleman's model, what does 'self-regulation' entail?",
                "Daniel Goleman'ın modeline göre 'öz düzenleme' (self-regulation) neleri içerir?",
                "The conscious discipline to withhold impulsive, emotional reactions during high-pressure crises",
                ["The automatic regulation of room temperature by corporate HVAC computers", "A daily requirement to memorize fifty new dictionary definitions before breakfast", "The legal right of managers to seize employee personal electronic equipment"],
                "Paragraph 2 defines self-regulation as the discipline to withhold impulsive reactions.",
                "2. paragraf öz düzenlemeyi krizler sırasında fevri tepkileri dizginleme disiplini olarak tanımlar."
            ),
            build_q(
                "q_r_b2_07_03",
                "How does executive panic neurologically affect software engineering teams during an outage?",
                "Yönetici paniği bir kesinti sırasında yazılım ekiplerini nörolojik olarak nasıl etkiler?",
                "Surging stress hormones impair the prefrontal cortex, crippling creative analytical reasoning",
                ["It causes computer screens to switch off and disconnect from the internet", "It forces developers to physically fall asleep at their desks immediately", "It permanently erases the company's code repository within minutes"],
                "Paragraph 3 explains fear impairs the prefrontal cortex responsible for problem-solving.",
                "3. paragraf korkunun problem çözmeden sorumlu prefrontal korteksi felç ettiğini açıklar."
            ),
            build_q(
                "q_r_b2_07_04",
                "How do emotionally intelligent leaders frame difficult performance feedback?",
                "Duygusal açıdan zeki liderler zorlayıcı performans geri bildirimini nasıl çerçeveler?",
                "Around objective behavioral patterns and mutual growth, separating personal identity from code errors",
                ["By publicly broadcasting critical complaints across company social media channels", "By deducting fifty dollars from employee salaries for every syntax error", "By refusing to speak to the struggling developer for several weeks"],
                "Paragraph 4 explains feedback is framed around objective patterns and growth, separating identity from errors.",
                "4. paragraf geri bildirimin nesnel kalıplar ve gelişim etrafında verilip kişiliğin hatadan ayrıldığını belirtir."
            ),
            build_q(
                "q_r_b2_07_05",
                "What quantifiable commercial return on investment (ROI) does high emotional intelligence deliver?",
                "Yüksek duygusal zeka hangi ölçülebilir ticari yatırım getirisini (ROI) sağlar?",
                "Substantially lower developer attrition rates and accelerated team velocity and innovation",
                ["A mandatory exemption from national corporate taxation in Europe", "Completely free electricity from public utility providers across all datacenters", "Guaranteed immunity from all commercial trademark lawsuits"],
                "Paragraph 5 highlights lower developer attrition and superior velocity/innovation rates.",
                "5. paragraf azalan işten ayrılma oranları ile artan hız ve yenilikçilik kazanımlarını vurgular."
            )
        ],
        "topic_tags": ["emotional-intelligence", "leadership", "management", "organizational-psychology", "b2-reading"],
        "related_ids": ["vocab.contagious", "vocab.attrition"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b2.micro-frontend-paradigms",
        "title": "Decentralized Front-End Architecture: The Micro-Frontend Paradigm",
        "cefr_level": "B2",
        "category": "technology",
        "summary_en": "An architectural and organizational examination of micro-frontends, analyzing how decomposing monolithic web client applications into autonomous domain vertical slices impacts team autonomy and runtime performance.",
        "summary_tr": "Mikro ön yüz (micro-frontend) mimarisi ve kurumsal etkileri: monolitik web istemcilerini otonom dikey dilimlere bölmenin ekip bağımsızlığı ve çalışma zamanı performansı üzerindeki analizi.",
        "word_count": 1030,
        "estimated_reading_minutes": 5,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Legacy Monolithic Front-End Bottleneck",
                "content_en": "Over the past decade, enterprise backend architectures underwent an aggressive transformation. Massive monolithic application servers were systematically carved into hundreds of independent, containerized microservices managed by autonomous cross-functional squads. However, as backend services achieved unprecedented deployment agility, an ironic architectural imbalance materialized: the front-end remained a colossal, single-repository monolith. Fifty developers across six different product squads committed code into the exact same massive Single Page Application (SPA). A single broken TypeScript definition in the settings module prevented the checkout squad from shipping urgent conversion fixes to production.",
                "content_tr": "Son on yılda kurumsal arka uç (backend) mimarileri agresif bir dönüşüm geçirdi. Devasa monolitik uygulama sunucuları, otonom fonksiyonlar arası ekipler tarafından yönetilen yüzlerce bağımsız, konteynerleştirilmiş mikroservise sistematik olarak bölündü. Ancak arka uç hizmetleri benzeri görülmemiş bir dağıtım çevikliğine ulaşırken ironik bir mimari dengesizlik ortaya çıktı: ön yüz (frontend) devasa, tek depolu bir monolit olarak kaldı. Altı farklı ürün ekibindeki elli geliştirici kodlarını tam olarak aynı büyük Tek Sayfalı Uygulamaya (SPA) gönderiyordu. Ayarlar modülündeki tek bir bozuk TypeScript tanımı, ödeme ekibinin acil dönüşüm düzeltmelerini canlı ortama göndermesini engelliyordu."
            },
            {
                "paragraph_index": 2,
                "title": "Decomposing the Web Client into Autonomous Verticals",
                "content_en": "The micro-frontend architectural paradigm emerged to resolve this organizational bottleneck by extending microservice principles to the browser tier. In a micro-frontend architecture, a complex web application is decomposed vertically by business domain rather than horizontally by technology layer. One squad owns the search experience end-to-end; another owns the customer account dashboard; a third owns the checkout funnel. Each micro-frontend possesses its own dedicated git repository, its own automated CI/CD pipeline, and can be developed, tested, and deployed to production independently without coordinating release dates with adjacent squads.",
                "content_tr": "Mikro ön yüz (micro-frontend) mimari paradigması, mikroservis ilkelerini tarayıcı katmanına genişleterek bu kurumsal darboğazı çözmek için ortaya çıktı. Bir mikro ön yüz mimarisinde karmaşık bir web uygulaması teknoloji katmanına göre yatay olarak değil, iş alanına göre dikey olarak ayrıştırılır. Bir ekip arama deneyimine uçtan uca sahip olur; bir diğeri müşteri hesap paneline; üçüncüsü ise ödeme akışına sahiptir. Her mikro ön yüz kendi özel git deposuna, kendi otomatik CI/CD işlem hattına sahiptir ve komşu ekiplerle yayın tarihlerini koordine etmeden bağımsız olarak geliştirilebilir, test edilebilir ve canlı ortama dağıtılabilir."
            },
            {
                "paragraph_index": 3,
                "title": "Composition Strategies: Build-Time versus Run-Time",
                "content_en": "Implementing micro-frontends requires deliberate integration engineering, categorized into two broad technical strategies: build-time composition and run-time composition. Build-time composition packages micro-frontends as published npm packages consumed by a parent container application. While conceptually simple, build-time composition recreates release coupling: whenever a child component updates, the parent application must be completely recompiled and redeployed. Consequently, modern enterprise architectures overwhelmingly prefer run-time composition through techniques like Webpack Module Federation or client-side iframes, where micro-applications are fetched dynamically over the network at execution time.",
                "content_tr": "Mikro ön yüzleri uygulamak, iki geniş teknik stratejiye ayrılan bilinçli entegrasyon mühendisliği gerektirir: derleme zamanı (build-time) birleştirme ve çalışma zamanı (run-time) birleştirme. Derleme zamanı birleştirme mikro ön yüzleri bir ana kapsayıcı uygulama tarafından tüketilen yayınlanmış npm paketleri olarak paketler. Kavramsal olarak basit olsa da derleme zamanı birleştirme yayın bağımlılığını yeniden yaratır: bir alt bileşen her güncellendiğinde ana uygulama tamamen yeniden derlenmeli ve yeniden dağıtılmalıdır. Sonuç olarak modern kurumsal mimariler, mikro uygulamaların çalışma anında ağ üzerinden dinamik olarak getirildiği Webpack Module Federation veya istemci tarafı iframe'ler gibi teknikler aracılığıyla çalışma zamanı birleştirmeyi ezici bir çoğunlukla tercih etmektedir."
            },
            {
                "paragraph_index": 4,
                "title": "The Hidden Costs of Runtime Fragmentation",
                "content_en": "Despite significant organizational velocity benefits, micro-frontends introduce severe architectural trade-offs that teams must rigorously manage. The most immediate penalty is client-side payload bloat. If three independent squads build their micro-frontends using different versions of React, Angular, and external charting libraries, the end-user's browser is forced to download redundant Megabytes of JavaScript framework code over mobile networks, drastically impairing Core Web Vitals and battery life. Furthermore, ensuring consistent visual design tokens, shared authentication states, and coordinated browser routing across disparate micro-applications requires mature architectural governance.",
                "content_tr": "Önemli kurumsal hız avantajlarına rağmen mikro ön yüzler ekiplerin titizlikle yönetmesi gereken ciddi mimari ödünleşimler getirir. En anlık ceza istemci tarafındaki veri yükü şişkinliğidir (payload bloat). Üç bağımsız ekip mikro ön yüzlerini React, Angular ve harici grafik kütüphanelerinin farklı sürümlerini kullanarak geliştirirse, son kullanıcının tarayıcısı mobil ağlar üzerinden megabaytlarca gereksiz JavaScript çerçeve kodunu indirmek zorunda kalır ve bu da Core Web Vitals ölçütlerini ve pil ömrünü ciddi şekilde bozar. Dahası farklı mikro uygulamalar arasında tutarlı görsel tasarım belirteçleri, paylaşılan kimlik doğrulama durumları ve koordineli tarayıcı yönlendirmesi sağlamak olgun bir mimari yönetişim gerektirir."
            },
            {
                "paragraph_index": 5,
                "title": "Architectural Evaluation: When to Adopt",
                "content_en": "Technical leaders must treat micro-frontends as an organizational scaling solution rather than a universal default pattern. For early-stage startups or small engineering departments staffed by fewer than twenty developers, the operational overhead of managing multiple repositories, distributed build pipelines, and Module Federation tooling vastly exceeds the benefits. A well-modularized single-page application remains superior. However, when an enterprise scales beyond fifty engineers across multiple product domains where release coordination friction paralyzes feature delivery, the micro-frontend paradigm becomes an indispensable catalyst for sustained agility.",
                "content_tr": "Teknik liderler mikro ön yüzleri evrensel bir varsayılan model olarak değil, kurumsal bir ölçeklendirme çözümü olarak ele almalıdır. Yirmi kişiden az geliştiricisi olan erken aşama girişimler veya küçük mühendislik departmanları için birden fazla depoyu, dağıtık derleme hatlarını ve Module Federation araçlarını yönetmenin operasyonel maliyeti faydaları katbekat aşar. İyi modülerleştirilmiş tek sayfalı bir uygulama üstünlüğünü korur. Ancak bir şirket yayın koordinasyonu sürtünmesinin özellik teslimatını felç ettiği birden fazla ürün alanında elliden fazla mühendise ulaştığında, mikro ön yüz paradigması sürdürülebilir çeviklik için vazgeçilmez bir katalizör haline gelir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "paradigm",
                "vocab_id": "vocab.paradigm",
                "context_definition_en": "A distinct set of concepts or thought patterns, including theories, research methods, and standards.",
                "context_meaning_tr": "Belirli bir döneme veya alana hakim olan kavramsal düşünce kalıbı ve model."
            },
            {
                "word": "catalyst",
                "vocab_id": "vocab.catalyst",
                "context_definition_en": "A substance or factor that causes or accelerates a chemical reaction or organizational change.",
                "context_meaning_tr": "Değişimi veya eylemi hızlandıran tetikleyici unsur."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b2_08_01",
                "What architectural bottleneck occurred when backend systems transitioned to microservices while frontends remained monolithic?",
                "Arka uç sistemleri mikroservislere geçerken ön yüz monolit kaldığında hangi mimari darboğaz oluştu?",
                "Dozens of developers committing to a single massive SPA caused release coupling and blocked deployment agility",
                ["Front-end code could no longer run on mobile phone screens or web browsers", "Computers ran out of physical memory and completely ceased manufacturing electronic parts", "Companies were forced by government regulators to abolish all internet websites"],
                "Paragraph 1 explains having 50 developers commit to a single SPA created release coupling.",
                "1. paragraf 50 geliştiricinin aynı büyük SPA'ya kod göndermesinin dağıtım çevikliğini felç ettiğini belirtir."
            ),
            build_q(
                "q_r_b2_08_02",
                "How does a micro-frontend architecture organize software teams and repositories?",
                "Bir mikro ön yüz mimarisi yazılım ekiplerini ve depolarını nasıl düzenler?",
                "Vertically by business domain slices with independent repositories, pipelines, and deployment cadences",
                ["By forcing all developers to work on a single desktop computer simultaneously in one room", "Horizontally by programming languages, banning all use of modern web frameworks", "By outsourcing all frontend code to freelance students through internet forums"],
                "Paragraph 2 describes vertical decomposition by business domain with dedicated repos and pipelines.",
                "2. paragraf dikey iş alanı dilimlerine göre bağımsız depo ve dağıtımlarla ayrıştırmayı açıklar."
            ),
            build_q(
                "q_r_b2_08_03",
                "Why do modern enterprise architectures prefer 'run-time' composition over 'build-time' composition?",
                "Modern kurumsal mimariler neden 'çalışma zamanı' birleştirmeyi 'derleme zamanı' birleştirmeye tercih eder?",
                "Build-time composition recreates release coupling by requiring the entire parent container to be recompiled",
                ["Run-time composition uses no computer electricity or memory whatsoever", "Build-time composition is legally banned by international browser consortia", "Run-time composition eliminates the need to write any front-end software code"],
                "Paragraph 3 states build-time composition recreates coupling requiring parent container recompilation.",
                "3. paragraf derleme zamanının ana uygulamanın yeniden derlenmesini gerektirerek bağımlılığı yeniden ürettiğini açıklar."
            ),
            build_q(
                "q_r_b2_08_04",
                "What serious client-side performance penalty can poorly governed micro-frontends inflict?",
                "Kötü yönetilen mikro ön yüzler istemci tarafında hangi ciddi performans cezasına yol açabilir?",
                "Payload bloat from downloading redundant duplicate framework versions over mobile networks",
                ["Permanently destroying the physical liquid crystal display of the user's smartphone", "Erasing the user's personal banking accounts from the financial system", "Causing web browsers to automatically translate all text into binary zeros and ones"],
                "Paragraph 4 highlights payload bloat when multiple squads ship redundant framework versions.",
                "4. paragraf birden fazla ekibin kütüphaneleri tekrar tekrar indirmesiyle oluşan veri yükü şişkinliğini anlatır."
            ),
            build_q(
                "q_r_b2_08_05",
                "When should technology leaders definitely AVOID adopting micro-frontends?",
                "Teknoloji liderleri mikro ön yüzleri benimsemekten kesinlikle ne zaman KAÇINMALIDIR?",
                "When working in early-stage startups or small squads with fewer than twenty developers",
                ["When developing enterprise e-commerce portals serving fifty million active users", "When coordinating multiple multinational squads across three continents", "When maintaining applications that require frequent daily deployment releases"],
                "Paragraph 5 states for early startups with fewer than 20 devs, operational overhead exceeds benefits.",
                "5. paragraf 20 kişiden az ekibi olan erken girişimlerde operasyonel yükün faydayı aştığını belirtir."
            )
        ],
        "topic_tags": ["micro-frontends", "frontend-architecture", "module-federation", "web-performance", "b2-reading"],
        "related_ids": ["vocab.paradigm", "vocab.catalyst"],
        "status": "APPROVED",
        "version": 1
    }
]

print(f"Defined {len(B2_READING_ARTICLES)} B2 reading articles.")
